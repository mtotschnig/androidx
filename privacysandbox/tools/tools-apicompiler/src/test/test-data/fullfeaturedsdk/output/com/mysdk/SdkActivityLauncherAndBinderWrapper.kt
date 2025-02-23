package com.mysdk

import android.os.Bundle
import androidx.privacysandbox.ui.core.SdkActivityLauncher
import androidx.privacysandbox.ui.provider.SdkActivityLauncherFactory

public class SdkActivityLauncherAndBinderWrapper private constructor(
    private val `delegate`: SdkActivityLauncher,
    public val launcherInfo: Bundle,
) : SdkActivityLauncher by delegate {
    public constructor(launcherInfo: Bundle) :
            this(SdkActivityLauncherFactory.fromLauncherInfo(launcherInfo), launcherInfo)
}
