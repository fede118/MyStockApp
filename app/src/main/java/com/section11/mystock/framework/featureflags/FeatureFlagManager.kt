package com.section11.mystock.framework.featureflags

/**
 * Manager for feature flags.
 *
 * It contains methods which return booleans representing if a feature is enabled or not
 */
interface FeatureFlagManager {

    /**
     * Feature flag for navigation to SingleStock screen. If its enabled tapping on a stock will
     * navigate to single stock screen. If its disabled it will show extra information on the same
     * screen, enlarging the card.
     */
    fun isNavigationToSingleStockEnabled(): Boolean
}

/**
 * For now is just going to have some hard coded flags. I want to implement a RemoteFeatureFlagManager
 * that calls a service, like Apiary, which returns a JSON with the configuration of the feature flags.
 * That way the value of the flag can be fetched at runtime and changed with the app "live"
 */
class LocalFeatureFlagManager: FeatureFlagManager {

    override fun isNavigationToSingleStockEnabled(): Boolean = false
}
