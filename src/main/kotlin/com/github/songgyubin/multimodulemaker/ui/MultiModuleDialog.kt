package com.github.songgyubin.multimodulemaker.ui

import com.github.songgyubin.multimodulemaker.MultiModuleCreator
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.Messages
import com.intellij.ui.dsl.builder.panel
import javax.swing.JComponent
import javax.swing.JTextField
/**
 *
 *
 * @author   Gyub
 * @created  2024/08/07
 */


class MultiModuleDialog(private val project: Project) : DialogWrapper(true) {
    private val moduleNamesField = JTextField()
    private val packageNamesField = JTextField()

    init {
        init()
        title = "Create Multiple Modules"
    }

    override fun createCenterPanel(): JComponent {
        return panel {
            row("Module Names (comma-separated):") {
                cell(moduleNamesField)
            }
            row("Package Names (comma-separated):") {
                cell(packageNamesField)
            }
        }
    }

    override fun doOKAction() {
        val moduleNames = moduleNamesField.text
        val packageNames = packageNamesField.text

        if (moduleNames.isEmpty() || packageNames.isEmpty()) {
            Messages.showErrorDialog(project, "Module names and package names must not be empty.", "Error")
            return
        }

        val moduleNamesList = moduleNames.split(",").map { it.trim() }
        val packageNamesList = packageNames.split(",").map { it.trim() }

        if (moduleNamesList.size != packageNamesList.size) {
            Messages.showErrorDialog(project, "The number of module names and package names must be the same.", "Error")
            return
        }

        MultiModuleCreator(project).createModules(moduleNamesList, packageNamesList)
        super.doOKAction()
    }
}