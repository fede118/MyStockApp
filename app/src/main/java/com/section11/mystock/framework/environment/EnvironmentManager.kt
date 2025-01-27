package com.section11.mystock.framework.environment

import android.content.SharedPreferences
import com.section11.mystock.BuildConfig
import javax.inject.Inject
import javax.inject.Singleton

private const val ENVIRONMENT_KEY = "environment_key"
private const val PROD_STRING = "Prod"
private const val TEST_STRING = "Test"

@Singleton
class EnvironmentManager @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {

    var currentEnvironment: Environment
        get() {
            // Retrieve the saved environment or default to Prod
            val environmentName = sharedPreferences.getString(ENVIRONMENT_KEY, Environment.Test.name)
            return Environment.fromString(environmentName ?: Environment.Test.name)
        }
        set(value) {
            // Save the current environment to SharedPreferences
            sharedPreferences.edit().putString(ENVIRONMENT_KEY, value.name).apply()
        }
    // Set the environment directly (e.g., from a UI or other parts of the app)
    fun setEnvironment(environment: Environment) {
        currentEnvironment = environment
    }

    fun getProdBaseUrl() = BuildConfig.SERP_API_BASE_URL

    fun getTestBaseUrl() = BuildConfig.TEST_API_BASE_URL

    sealed class Environment {
        data object Prod : Environment(){
            override val name: String
                get() = PROD_STRING
        }
        data object Test : Environment() {
            override val name: String
                get() = TEST_STRING
        }

        abstract val name: String

        companion object {
            fun fromString(value: String): Environment {
                return when (value) {
                    PROD_STRING -> Prod
                    TEST_STRING -> Test
                    else -> Test
                }
            }
        }
    }
}
