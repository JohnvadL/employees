package com.square.contacts

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner


@RunWith(RobolectricTestRunner::class)
class EmployeesDomainTest {

    private val mockURL = "/employee/list"
    private lateinit var employeeDomain: EmployeesDomain
    private lateinit var mockWebServer: MockWebServer

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
        val url = mockWebServer.url(mockURL)
        employeeDomain = EmployeesDomain(url.toString())
    }

    @After
    fun tearDown() {
        mockWebServer.close()
    }

    @Test
    fun testHappyPath() {
        var receivedData: ArrayList<Employee>? = null
        val mockedResponse = MockResponse()
            .setBody(simpleJson)
            .addHeader("Content-Type", "application/json")
        var receivedError: String? = null

        mockWebServer.enqueue(mockedResponse)
        employeeDomain.getAllContacts({
            receivedData = it
        }, {
            receivedError = it
        })

        Thread.sleep(200)
        Assert.assertNotNull(receivedData)
        Assert.assertEquals("name 1", receivedData!![0].name, )
        Assert.assertEquals("uuid1", receivedData!![0].id, )
        Assert.assertEquals("Team name ", receivedData!![0].team, )
        Assert.assertNull(receivedError)
    }

    @Test
    fun testMissingName() {
        var receivedData: ArrayList<Employee>? = null
        var receivedError: String? = null

        val mockedResponse = MockResponse()
            .setBody(missingName)
            .addHeader("Content-Type", "application/json")

        mockWebServer.enqueue(mockedResponse)
        employeeDomain.getAllContacts({
            receivedData = it
        }, {
            receivedError = it
        })

        Thread.sleep(200)
        Assert.assertNotNull(receivedData)
        Assert.assertEquals(0, receivedData!!.size)
        Assert.assertNull(receivedError)
    }

    @Test
    fun testIncorrectJson() {
        var receivedData: ArrayList<Employee>? = null
        var receivedError: String? = null

        val mockedResponse = MockResponse()
            .setBody(errorJson)
            .addHeader("Content-Type", "application/json")

        mockWebServer.enqueue(mockedResponse)
        employeeDomain.getAllContacts({
            receivedData = it
        }, {
            receivedError = it
        })

        Thread.sleep(200)
        Assert.assertNull(receivedData)
        Assert.assertNotNull(receivedError)
    }

    @Test
    fun testOneMalformedEmployee(){
        var receivedData: ArrayList<Employee>? = null
        val mockedResponse = MockResponse()
            .setBody(oneValidEmployee)
            .addHeader("Content-Type", "application/json")
        var receivedError: String? = null

        mockWebServer.enqueue(mockedResponse)
        employeeDomain.getAllContacts({
            receivedData = it
        }, {
            receivedError = it
        })
        Thread.sleep(200)
        Assert.assertNotNull(receivedData)
        Assert.assertEquals(1 , receivedData!!.size, )
        Assert.assertEquals("name 2", receivedData!![0].name)
        Assert.assertEquals("uuid2", receivedData!![0].id, )
        Assert.assertEquals("Team 2", receivedData!![0].team, )
        Assert.assertNull(receivedError)
    }


    private val simpleJson = """
{
	"employees" : [
		{
      "uuid" : "uuid1",
      "full_name" : "name 1",
      "phone_number" : "11223344",
      "email_address" : "email@email.com",
      "team" : "Team name ",
      "employee_type" : "FULL_TIME"
    }
	]
}
    """

    private val missingName = """
{
	"employees" : [
		{
      "uuid" : "uuid1",
      "phone_number" : "11223344",
      "email_address" : "email@email.com",
      "team" : "Team name ",
      "employee_type" : "FULL_TIME"
    }
	]
}
    """

    private val errorJson = """
{
	"employees" : [
		{
      "uuid" : "uuid1",
      "phone_number" : "11223344",
      "email_address" : "email@email.com",
}
    """

    private val oneValidEmployee ="""
{
  "employees": [
    {
      "phone_number": "11223344",
      "email_address": "email@email.com",
      "team": "Team name ",
      "employee_type": "FULL_TIME"
    },
    {
      "uuid": "uuid2",
      "full_name": "name 2",
      "phone_number": "22222222",
      "email_address": "email2@email.com",
      "team": "Team 2",
      "employee_type": "CONTRACT"
    }
  ]
}
    """
}