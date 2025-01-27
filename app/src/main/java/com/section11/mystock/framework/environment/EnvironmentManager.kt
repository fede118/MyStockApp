package com.section11.mystock.framework.environment

import android.content.SharedPreferences
import com.section11.mystock.BuildConfig
import javax.inject.Inject
import javax.inject.Singleton

private const val ENVIRONMENT_KEY = "environment_key"
private const val PROD_STRING = "Prod"
private const val TEST_STRING = "Test"

@Singleton
class EnvironmentManager @Inject constructor(private val sharedPreferences: SharedPreferences) {

    var currentEnvironment: Environment
        get() {
            val environmentName = sharedPreferences.getString(ENVIRONMENT_KEY, Environment.Test.name)
            return Environment.fromString(environmentName ?: Environment.Test.name)
        }
        set(value) {
            sharedPreferences.edit().putString(ENVIRONMENT_KEY, value.name).apply()
        }

    fun setEnvironment(environment: Environment) {
        currentEnvironment = environment
    }

    sealed class Environment {
        data object Prod : Environment(){
            override val name = PROD_STRING
            override val baseUrl = BuildConfig.SERP_API_BASE_URL
        }
        data object Test : Environment() {
            override val name: String = TEST_STRING
            override val baseUrl = BuildConfig.TEST_API_BASE_URL
        }

        abstract val name: String

        abstract val baseUrl: String

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
