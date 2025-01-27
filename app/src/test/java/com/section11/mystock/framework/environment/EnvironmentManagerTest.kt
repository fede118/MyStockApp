package com.section11.mystock.framework.environment

import android.content.SharedPreferences
import com.section11.mystock.BuildConfig
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.anyString
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.kotlin.whenever

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

        assertEquals(EnvironmentManager.Environment.Test, environment)
    }

    @Test
    fun `should return Prod environment when saved in SharedPreferences`() {
        whenever(sharedPreferences.getString(anyString(), anyString()))
            .thenReturn(EnvironmentManager.Environment.Prod.name)

        val environment = environmentManager.currentEnvironment

        assertEquals(EnvironmentManager.Environment.Prod, environment)
    }

    @Test
    fun `setEnvironment should save environment to SharedPreferences`() {
        environmentManager.setEnvironment(EnvironmentManager.Environment.Prod)

        verify(sharedPreferencesEditor)
            .putString("environment_key", EnvironmentManager.Environment.Prod.name)
        verify(sharedPreferencesEditor).apply()
    }

    @Test
    fun `currentEnvironment should update after setting new environment`() {
        whenever(sharedPreferences.getString(anyString(), anyString()))
            .thenReturn(EnvironmentManager.Environment.Test.name)

        environmentManager.setEnvironment(EnvironmentManager.Environment.Prod)

        whenever(sharedPreferences.getString(anyString(), anyString()))
            .thenReturn(EnvironmentManager.Environment.Prod.name)
        val environment = environmentManager.currentEnvironment

        assertEquals(EnvironmentManager.Environment.Prod, environment)
    }

    @Test
    fun `getProdBaseUrl should return correct base URL`() {
        assertEquals(BuildConfig.SERP_API_BASE_URL, environmentManager.getProdBaseUrl())
    }

    @Test
    fun `getTestBaseUrl should return correct base URL`() {
        assertEquals(BuildConfig.TEST_API_BASE_URL, environmentManager.getTestBaseUrl())
    }

    @Test
    fun `fromString should return correct Environment for valid input`() {
        val prodEnvironment = EnvironmentManager.Environment.fromString("Prod")
        val testEnvironment = EnvironmentManager.Environment.fromString("Test")

        assertEquals(EnvironmentManager.Environment.Prod, prodEnvironment)
        assertEquals(EnvironmentManager.Environment.Test, testEnvironment)
    }

    @Test
    fun `fromString should default to Test for invalid input`() {
        val invalidEnvironment = EnvironmentManager.Environment.fromString("Invalid")

        assertEquals(EnvironmentManager.Environment.Test, invalidEnvironment)
    }
}
