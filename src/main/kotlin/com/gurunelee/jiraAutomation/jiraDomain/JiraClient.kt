package com.gurunelee.jiraAutomation.jiraDomain

import com.atlassian.jira.rest.client.api.JiraRestClient
import com.atlassian.jira.rest.client.api.domain.Issue
import com.atlassian.jira.rest.client.api.domain.IssueType
import com.atlassian.jira.rest.client.api.domain.input.FieldInput
import com.atlassian.jira.rest.client.api.domain.input.IssueInputBuilder
import com.atlassian.jira.rest.client.api.domain.input.LinkIssuesInput
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory
import com.atlassian.jira.rest.client.internal.json.gen.LinkIssuesInputGenerator
import java.net.URL


class JiraClient(
        username: String,
        password: String,
        domain: String,
) {
    private val restClient = getJiraRestClient(username, password, domain);

    fun searchIssues(jql: String): List<String> = restClient.searchClient.searchJql(jql).claim().issues.map { it.key }
    fun createIssue(projectKey: String, issueType: Long, summary: String): String {
        val issueInput = IssueInputBuilder(projectKey, issueType, summary).build()
        return restClient.issueClient.createIssue(issueInput).claim().key
    }

    fun getIssue(issueKey: String): Issue = restClient.issueClient
            .getIssue(issueKey)
            .claim()

    fun updateIssue(issueKey: String, fieldId: String, value: Any) {
        val fieldInput = FieldInput(fieldId, value)
        val issueInput = IssueInputBuilder()
                .setFieldInput(fieldInput)
                .build()
        restClient.issueClient.updateIssue(issueKey, issueInput).claim()
    }

    fun linkIssue(formIssueKey:String, toIssueKey: String, issueType: String) {
        restClient.issueClient.linkIssue(
                LinkIssuesInput(formIssueKey, toIssueKey, issueType)
        )
    }

    fun deleteIssue(issueKey: String, deleteSubtasks: Boolean): Void = restClient.issueClient
            .deleteIssue(issueKey, deleteSubtasks)
            .claim()


    private fun getJiraRestClient(username: String, password: String, domain: String): JiraRestClient = AsynchronousJiraRestClientFactory().createWithBasicHttpAuthentication(
            URL(domain).toURI(),
            username,
            password,
    )
}