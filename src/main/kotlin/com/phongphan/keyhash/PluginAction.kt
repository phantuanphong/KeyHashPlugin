package com.phongphan.keyhash

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.ui.Messages
import java.awt.BorderLayout
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection
import java.util.*
import javax.swing.JLabel
import javax.swing.JOptionPane
import javax.swing.JPanel
import com.intellij.openapi.util.IconLoader

class PluginAction: AnAction() {

    override fun update(e: AnActionEvent) {
        super.update(e)
    }

    override fun actionPerformed(e: AnActionEvent) {

        val inputRegex = Regex("^[a-fA-F0-9]{2}(:[a-fA-F0-9]{2}){19}$")
        val message = "Enter Sha1:"
        val defaultValue = ""
        val icon = IconLoader.getIcon("/icons/icon.svg")
        val input = Messages.showInputDialog(null, message, "Facebook Key Hash Calculator", icon, defaultValue, null)

        if (!input.isNullOrEmpty()  && inputRegex.matches(input)) {

            val bytesList = mutableListOf<Byte>()
            for (i in input.indices step 3) {
                val b = input.substring(i, i + 2).toInt(16).toByte()
                bytesList.add(b)
            }
            val bytes = ByteArray(bytesList.size)
            for (i in bytesList.indices) {
                bytes[i] = bytesList[i]
            }
            val encodedString = Base64.getEncoder().encodeToString(bytes)

            val confirmationMessage = String.format("$encodedString", input)

            val panel = JPanel(BorderLayout())
            val messageLabel = JLabel(confirmationMessage)
            panel.add(messageLabel, BorderLayout.CENTER)

            val optionPaneResult = JOptionPane.showOptionDialog(
                    null,
                    panel,
                    "Key hash",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    icon,
                    arrayOf("Copy to clipboard", "Close"),
                    null
            )

            if (optionPaneResult == JOptionPane.YES_OPTION) {
                // Copy the message to the clipboard
                val clipboard = Toolkit.getDefaultToolkit().systemClipboard
                val selection = StringSelection(confirmationMessage)
                clipboard.setContents(selection, null)
            }
        }else {
            if(input != null){
                // Invalid input, show an error message
                Messages.showMessageDialog("Invalid input", "Error", Messages.getErrorIcon())
            }
        }

    }

    //Override getActionUpdateThread() when you target 2022.3 or later!
//    override fun getActionUpdateThread(): ActionUpdateThread {
//        return super.getActionUpdateThread()
//    }
}