package com.github.songgyubin.multimodulemaker.action

import com.github.songgyubin.multimodulemaker.ui.MultiModuleDialog
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent

/**
 *
 *
 * @author   Gyub
 * @created  2024/08/07
 */
class MultiModuleAction : AnAction() {
    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project ?: return
        val dialog = MultiModuleDialog(project)
        dialog.show()
    }
}