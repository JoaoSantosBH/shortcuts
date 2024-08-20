package com.jomar.poc.quickactionsbrq

enum class ShortcutTypeEnum(val type: String, val description: String, val label: String) {
    STATIC("Static","Atalho estático foi clicado","Adicionar Shortcut Estática"),
    DYNAMIC("Dynamic","Atalho dinâmico foi clicado","Adicionar Shortcut Dinamica"),
    PINNED("Pinned","Atalho pinado foi clicado","Adicionar Shortcut Pinned")
}