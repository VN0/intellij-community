// Copyright 2000-2020 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package com.jetbrains.python.sdk.configuration

import com.intellij.codeInspection.util.IntentionName
import com.intellij.openapi.extensions.ExtensionPointName
import com.intellij.openapi.module.Module
import com.intellij.openapi.projectRoots.Sdk
import com.intellij.util.concurrency.annotations.RequiresBackgroundThread
import org.jetbrains.annotations.ApiStatus

@ApiStatus.Experimental
interface PyProjectSdkConfigurationExtension {

  companion object {
    @JvmStatic
    val EP_NAME = ExtensionPointName.create<PyProjectSdkConfigurationExtension>("Pythonid.projectSdkConfigurationExtension")
  }

  /**
   * Called by sdk configurator and interpreter inspection
   * to determine if an extension could configure or suggest an interpreter for the passed [module].
   *
   * First applicable extension is processed, others are ignored.
   * If there is no applicable extension, configurator and inspection guess a suitable interpreter.
   *
   * Could be called from AWT hence should be as fast as possible.
   */
  fun isApplicable(module: Module): Boolean

  /**
   * An implementation is responsible for interpreter setup and registration in IDE.
   * In case of failures `null` should be returned, the implementation is responsible for errors displaying.
   *
   * Rule of thumb is to explicitly ask a user if sdk creation is desired and allowed.
   */
  @RequiresBackgroundThread
  fun createAndAddSdkForConfigurator(module: Module): Sdk?

  /**
   * Returned string is used as a quick fix name.
   * Example: `Create a virtual environment using requirements.txt`.
   */
  @IntentionName
  fun getIntentionName(module: Module): String

  /**
   * An implementation is responsible for interpreter setup and registration in IDE.
   * In case of failures `null` should be returned, the implementation is responsible for errors displaying.
   *
   * You're free here to create sdk immediately, without any user permission since quick fix is explicitly clicked.
   */
  @RequiresBackgroundThread
  fun createAndAddSdkForInspection(module: Module): Sdk?
}