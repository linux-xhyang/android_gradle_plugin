/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.build.gradle.internal.tasks

import com.android.build.gradle.internal.DependencyChecker
import com.android.utils.Pair
import org.gradle.api.tasks.TaskAction

public class PrepareDependenciesTask extends BaseTask {
    final List<DependencyChecker> checkers = []
    final Set<Pair<Integer, String>> androidDependencies = []

    void addDependency(Pair<Integer, String> api) {
        androidDependencies.add(api)
    }

    @TaskAction
    protected void prepare() {
        def minSdkVersion = variant.config.minSdkVersion

        for (DependencyChecker checker : checkers) {
            for (Integer api : checker.foundAndroidApis) {
                if (api > minSdkVersion) {
                    throw new RuntimeException(String.format(
                            "ERROR: %s has an indirect dependency on Android API level %d, but minSdkVersion for variant '%s' is API level %d",
                            checker.configName.capitalize(), api, variant.name, minSdkVersion))
                }
            }

        }
    }

    def addChecker(DependencyChecker checker) {
        checkers.add(checker)
    }
}
