/*
 *
 * 프로그램에 대한 저작권을 포함한 지적재산권은 (주)씨알에스큐브에 있으며, (주)씨알에스큐브가 명시적으로 허용하지 않은
 * 사용, 복사, 변경, 제3자에의 공개, 배포는 엄격히 금지되며, (주)씨알에스큐브의 지적 재산권 침해에 해당됩니다.
 * Copyright ⓒ 2022. CRScube Co., Ltd. All Rights Reserved| Confidential
 */

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by khyou on 2020-05-22.
 */
public class VisitReportLetterResponse {


    private Long mvrLetterKey;

    private Long tscKey;

    private String letterType;

    private String subject;

    private String contents;

    private String inputTime;

    private String address;

    private String recpType;

    private String userId;

    private String userName;

    private String email;

    public Long getMvrLetterKey() {
        return mvrLetterKey;
    }

    public void setMvrLetterKey(Long mvrLetterKey) {
        this.mvrLetterKey = mvrLetterKey;
    }

    public Long getTscKey() {
        return tscKey;
    }

    public void setTscKey(Long tscKey) {
        this.tscKey = tscKey;
    }

    public String getLetterType() {
        return letterType;
    }

    public void setLetterType(String letterType) {
        this.letterType = letterType;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContents(List<Object> inlineFiles) {
        // self closing tag로 변환 처리는 필수임
        final String selfClosedTags = convertToSelfClosingTags(replaceContents(contents, inlineFiles));
        try {
            return removeFontFamily(convertToPTag(selfClosedTags));
        } catch (Exception e) {
            return selfClosedTags;
        }
    }

    public String convertToSelfClosingTags(String htmlString) {
        Pattern pattern = Pattern.compile("<(col|hr|br|img)([^>]*)\\s*(?<!/)>");
        Matcher matcher = pattern.matcher(htmlString);
        StringBuffer result = new StringBuffer();

        while (matcher.find()) {
            if (!isColgroupTag(matcher)) {
                String replacement = "<" + matcher.group(1) + matcher.group(2) + " />";
                matcher.appendReplacement(result, replacement);
            } else {
                matcher.appendReplacement(result, matcher.group());
            }
        }

        matcher.appendTail(result);
        return result.toString();
    }

    public String convertToPTag(String htmlString) {
        Pattern pattern = Pattern.compile("<mark(.*?)>(.*?)</mark>", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(htmlString);

        // 문자열 내에서 패턴을 찾아 p 태그로 변환
        return matcher.replaceAll("<span$1>$2</span>");
    }

    public String removeFontFamily(String htmlString) {
        // Pattern innerStyle = Pattern.compile("(?<=style=['\"])(.*?)(?=['\"])", Pattern.MULTILINE);
        Pattern innerStyle = Pattern.compile("<[^>]+?\\s+(style\\s*=\\s*['\"]([^'\"]*?))['\"][^>]*?>");
        Matcher innerStyleMatcher = innerStyle.matcher(htmlString);
        while(innerStyleMatcher.find()) {
            String group = innerStyleMatcher.group(1);
            String replacement = group.replaceAll("&quot;", "") // '맑은 고딕' 등의 폰트가 들어가면 큰따옴표가 들어가는데 이를 제거
                                      .replaceAll("mso(.*?):[^;']*(;)?", "") // remove MS-WORD style group
                                      .replaceAll("font-family:[^;']*(;)?", "");
            htmlString = htmlString.replace(group, replacement);
        }
        
        return htmlString;
    }

    private boolean isColgroupTag(Matcher matcher) {
        return matcher.group(1).equalsIgnoreCase("col") && matcher.group(2).equalsIgnoreCase("group");
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getInputTime() {
        return inputTime;
    }

    public void setInputTime(String inputTime) {
        this.inputTime = inputTime;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRecpType() {
        return recpType;
    }

    public void setRecpType(String recpType) {
        this.recpType = recpType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * 1) Legacy summernote img tag 형태
     * <img src='/project.vr-covr/khyou_beta_test_project/files/downloads?fileName=/webapp/cubeCTMS/beta/annexes/mvr-letter/52f163da-fa02-4878-8260-2c19762420cc_2023-10-13_17-35-25.png'>
     *
     * 2) CTMS V1.2 tiptap img tag 형태
     * <img src="r-eo4bc"
     * id="{s3}d0d3752e-d196-4c45-af39-3481f2ef74b6/crn:app_ctms:stg-beta:4c681bb0-6846-40ba-bcf3-2301a251264d"
     * width="500">
     */
    public String replaceContents(String contents, List<Object> inlineFiles) {
        // if (inlineFiles.isEmpty()) {
        //     return contents;
        // }

        Pattern imgPattern = Pattern.compile("<img[^>]*>");
        Pattern idPattern = Pattern.compile("id=(?:\"|\')([^\"\']+)(?:\"|\')");
        Pattern srcPattern = Pattern.compile("src=(?:\"|\')([^\"\']+)(?:\"|\')");
        Matcher imgMatcher = imgPattern.matcher(contents);

        int index = 0;
        try {
            while (imgMatcher.find()) {
                String imgTag = imgMatcher.group(0);

                Matcher idMatcher = idPattern.matcher(imgTag);
                Matcher srcMatcher = srcPattern.matcher(imgTag);
                String id = "";
                String src = "";
                if (srcMatcher.find()) {
                    src = srcMatcher.group(1);
                    if (idMatcher.find()) {
                        id = idMatcher.group(1); // S3
                    } else {
                        id = extractFileName(src); // EFS
                    }
                }

                String replacedImgSrc = "";
                // if (id.startsWith("{s3}")) {
                //     final String batchEfsPath = EfsUtils.toBatchEfsPath(inlineFiles.get(index).getFileId());
                //     if (batchEfsPath != null) {
                //         final String[] srcArr = batchEfsPath.split(annexes);
                //         replacedImgSrc = imgTag.replace(src, "/" + annexes + srcArr[1]);
                //     }
                // } else {
                //     final String[] srcArr = src.split(annexes);
                //     replacedImgSrc = imgTag.replace(src, "/" + annexes + srcArr[1]);
                // }
                contents = contents.replace(imgTag, replacedImgSrc);
                index++;
            }
        } catch (Exception e) {
            return contents;
        }
        return contents;
    }

    public static String extractFileName(String src) {
        Pattern pattern = Pattern.compile("fileName=([^&]+)");
        Matcher matcher = pattern.matcher(src);
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return "";
        }
    }
}
