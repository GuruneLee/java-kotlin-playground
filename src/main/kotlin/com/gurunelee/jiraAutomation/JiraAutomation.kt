package com.gurunelee.jiraAutomation

import com.gurunelee.jiraAutomation.jiraDomain.JiraClient
import com.gurunelee.logger.log

/**
 * Created by chlee on 3/16/24.
 *
 * @author Changha Lee
 * @version java-kotlin-playground
 * @since java-kotlin-playground
 */
fun main() {
    // init
    val userName = "chlee4858"
    val password = ""

    val jiraClient = JiraClient(userName, password, "http://localhost:8080")

    val issues = jiraClient.searchIssues("assignee = \"chlee4858\"")

    issues.forEach {it.log()}

    jiraClient.linkIssue("PROJ-1", "PROJ-2", "Cloners")
}
