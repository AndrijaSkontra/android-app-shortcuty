package org.rma.unizd.skontra

data class ShortcutMoshi(
    val shortDescription: String,
    val editorsUsing: List<String>,
    val shortcut: String,
    val howToRemember: String
)
