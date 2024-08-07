package com.github.songgyubin.multimodulemaker

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VfsUtil
import java.io.File

/**
 *
 *
 * @author   Gyub
 * @created  2024/08/07
 */
class MultiModuleCreator(private val project: Project) {

    fun createModules(moduleNames: List<String>, packageNames: List<String>) {
        for ((moduleName, packageName) in moduleNames.zip(packageNames)) {
            createModule(moduleName, packageName)
        }
    }

    private fun createModule(moduleName: String, packageName: String) {
        val projectBaseDir = project.basePath ?: return
        val moduleDir = "$projectBaseDir/$moduleName"
        val moduleDirFile = File(moduleDir)

        if (!moduleDirFile.exists()) {
            moduleDirFile.mkdirs()
        }

        createBuildGradleFile(moduleDirFile, moduleName, packageName)
        createManifestFile(moduleDirFile, packageName)
        createSourceDirs(moduleDirFile, packageName)
        refreshProject(moduleDirFile)
    }

    private fun createBuildGradleFile(moduleDir: File, moduleName: String, packageName: String) {
        val buildGradleContent = """
            plugins {
                alias(libs.plugins.android.library)
                alias(libs.plugins.jetbrains.kotlin.android)
            }

            android {
                namespace = $packageName
                compileSdk = 34

            defaultConfig {
               minSdk = 26

               testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
               consumerProguardFiles("consumer-rules.pro")
            }

                buildTypes {
                    release {
                        minifyEnabled false
                        proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
                    }
                }

                 buildTypes {
                     release {
                        isMinifyEnabled = false
                        proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
                    }
                }
                compileOptions {
                    sourceCompatibility = JavaVersion.VERSION_1_8
                    targetCompatibility = JavaVersion.VERSION_1_8
                }
                kotlinOptions {
                    jvmTarget = "1.8"
                }
            }

            dependencies {
              // TODO: Add To Dependencies
            }
        """.trimIndent()

        val buildGradleFile = File(moduleDir, "build.gradle.kts")
        buildGradleFile.writeText(buildGradleContent)
    }

    private fun createManifestFile(moduleDir: File, packageName: String) {
        val manifestDir = File(moduleDir, "src/main")
        manifestDir.mkdirs()

        val manifestContent = """
            <manifest xmlns:android="http://schemas.android.com/apk/res/android"
                package="$packageName">
                
                <application>
                </application>
            </manifest>
        """.trimIndent()

        val manifestFile = File(manifestDir, "AndroidManifest.xml")
        manifestFile.writeText(manifestContent)
    }

    private fun createSourceDirs(moduleDir: File, packageName: String) {
        val packagePath = packageName.replace('.', '/')
        val mainSourceDir = File(moduleDir, "src/main/java/$packagePath")
        val testSourceDir = File(moduleDir, "src/test/java/$packagePath")

        mainSourceDir.mkdirs()
        testSourceDir.mkdirs()
    }

    private fun refreshProject(moduleDir: File) {
        LocalFileSystem.getInstance().refreshAndFindFileByIoFile(moduleDir)?.let {
            VfsUtil.markDirtyAndRefresh(false, true, true, it)
        }
    }
}