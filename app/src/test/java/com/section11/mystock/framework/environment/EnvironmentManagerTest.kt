package com.section11.mystock.framework.environment

import android.content.SharedPreferences
import com.section11.mystock.BuildConfig
import com.section11.mystock.framework.environment.EnvironmentManager.Environment
import com.section11.mystock.framework.environment.EnvironmentManager.Environment.Prod
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.anyString
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class EnvironmentManagerTest {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var sharedPreferencesEditor: SharedPreferences.Editor
    private lateinit var environmentManager: EnvironmentManager

    @Before
    fun setup() {
        sharedPreferences = mock(SharedPreferences::class.java)
        sharedPreferencesEditor = mock(SharedPreferences.Editor::class.java)

        whenever(sharedPreferences.edit()).thenReturn(sharedPreferencesEditor)
        whenever(sharedPreferencesEditor.putString(anyString(), anyString()))
            .thenReturn(sharedPreferencesEditor)

        environmentManager = EnvironmentManager(sharedPreferences)
    }

    @Test
    fun `default environment should be Test when SharedPreferences has no value`() {
        whenever(sharedPreferences.getString(anyString(), anyString())).thenReturn(null)

        val environment = environmentManager.currentEnvironment

        assertEquals(Environment.Test, environment)
    }

    @Test
    fun `should return Prod environment when saved in SharedPreferences`() {
        whenever(sharedPreferences.getString(anyString(), anyString()))
            .thenReturn(Prod.name)

        val environment = environmentManager.currentEnvironment

        assertEquals(Prod, environment)
    }

    @Test
    fun `setEnvironment should save environment to SharedPreferences`() = runTest {
        environmentManager.setEnvironment(Prod)
        advanceUntilIdle()

        verify(sharedPreferencesEditor)
            .putString("environment_key", Prod.name)
        verify(sharedPreferencesEditor).apply()
    }

    @Test
    fun `currentEnvironment should update after setting new environment`() {
        whenever(sharedPreferences.getString(anyString(), anyString()))
            .thenReturn(Environment.Test.name)

        environmentManager.setEnvironment(Prod)

        whenever(sharedPreferences.getString(anyString(), anyString()))
            .thenReturn(Prod.name)
        val environment = environmentManager.currentEnvironment

        assertEquals(Prod, environment)
    }

    @Test
    fun `fromString should return correct Environment for valid input`() {
        val prodEnvironment = Environment.fromString("Prod")
        val testEnvironment = Environment.fromString("Test")

        assertEquals(Prod, prodEnvironment)
        assertEquals(Environment.Test, testEnvironment)
    }

    @Test
    fun `fromString should default to Test for invalid input`() {
        val invalidEnvironment = Environment.fromString("Invalid")

        assertEquals(Environment.Test, invalidEnvironment)
    }

    @Test
    fun `prod baseUrl should be serpapi defined in build config`() {
        assertEquals(BuildConfig.SERP_API_BASE_URL, Prod.baseUrl)
    }

    @Test
    fun `test url should be testBaseUrl defined in build config`() {
        assertEquals(BuildConfig.TEST_API_BASE_URL, Environment.Test.baseUrl)
    }
}
