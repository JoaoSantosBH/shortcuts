package com.jomar.poc.quickactionsbrq

import android.app.PendingIntent
import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.content.getSystemService
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import com.jomar.poc.quickactionsbrq.ui.theme.QuickActionsBRQTheme

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handleIntent(intent)

        enableEdgeToEdge()
        setContent {
            QuickActionsBRQTheme {
                MyQuickScreen()
            }
        }
    }

    @Composable
    private fun MyQuickScreen() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Red),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(
                16.dp, Alignment.CenterVertically
            )
        ) {
            when (viewModel.shortcutType) {
                ShortcutTypeEnum.STATIC -> Text(ShortcutTypeEnum.STATIC.description)
                ShortcutTypeEnum.DYNAMIC -> Text(ShortcutTypeEnum.DYNAMIC.description)
                ShortcutTypeEnum.PINNED -> Text(ShortcutTypeEnum.PINNED.description)
                null -> Unit
            }
            Button(
                onClick = ::addDynamicShortcut
            ) {
                Text(ShortcutTypeEnum.DYNAMIC.label)
            }
            Button(
                onClick = ::addPinnedShortcut
            ) {
                Text(ShortcutTypeEnum.PINNED.label)
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun addPinnedShortcut() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) { return }
        val shortcutManager = getSystemService<ShortcutManager>()!!
        if (shortcutManager.isRequestPinShortcutSupported) {
            val shortcut = ShortcutInfo.Builder(applicationContext, ShortcutTypeEnum.PINNED.type)
                .setShortLabel("Abrir Home do app")
                .setLongLabel("Uma mensagem pra home")
                .setIcon(
                    Icon.createWithResource(
                        applicationContext, R.drawable.baseline_app_shortcut_24
                    )
                )
                .setIntent(
                    Intent(applicationContext, MainActivity::class.java).apply {
                        action = Intent.ACTION_VIEW
                        putExtra(SHORTCUT_ID, ShortcutTypeEnum.PINNED.type)
                    }
                )
                .build()

            val callbackIntent = shortcutManager.createShortcutResultIntent(shortcut)
            val successPendingIntent = PendingIntent.getBroadcast(
                applicationContext,
                0,
                callbackIntent,
                PendingIntent.FLAG_IMMUTABLE
            )
            shortcutManager.requestPinShortcut(shortcut, successPendingIntent.intentSender)
        }
    }

    private fun addDynamicShortcut() {
        val uri = Uri.parse("https://wa.me/5511970586144?text=${Uri.encode("Ola")}")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        val shortcut = ShortcutInfoCompat.Builder(applicationContext, ShortcutTypeEnum.DYNAMIC.type)
            .setShortLabel("Whatsapp")
            .setLongLabel("Clicando aqui vai abrir o zap")
            .setIcon(
                IconCompat.createWithResource(
                    applicationContext, R.drawable.baseline_app_shortcut_24
                )
            )
            .setIntent(intent)
            .build()
        ShortcutManagerCompat.pushDynamicShortcut(applicationContext, shortcut)
    }

    private fun handleIntent(intent: Intent?) {
        intent?.let {
            when (intent.getStringExtra(SHORTCUT_ID)) {
                ShortcutTypeEnum.STATIC.type -> viewModel.onShortcutClicked(ShortcutTypeEnum.STATIC)
                ShortcutTypeEnum.DYNAMIC.type -> viewModel.onShortcutClicked(ShortcutTypeEnum.DYNAMIC)
                ShortcutTypeEnum.PINNED.type -> viewModel.onShortcutClicked(ShortcutTypeEnum.PINNED)
            }
        }
    }

    companion object {
        const val SHORTCUT_ID = "shortcut_id"
    }
}


