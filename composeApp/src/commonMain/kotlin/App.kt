import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import com.nikulin.lines.di.initKoin
import com.nikulin.lines.presentation.localcomposition.LocalNavigator
import com.nikulin.lines.presentation.navigation.AppNavHost
import com.nikulin.lines.presentation.navigation.rememberAppNavigator
import com.nikulin.lines.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App(
    isDarkTheme: Boolean,
    dynamicColor: Boolean,
) {
    initKoin()

    val layoutDirection = LocalLayoutDirection.current
    val navigator = rememberAppNavigator()

    AppTheme(
        isDarkTheme = isDarkTheme,
        dynamicColor = dynamicColor,
    ) {
        CompositionLocalProvider(
            LocalNavigator provides navigator
        ) {
            Scaffold(
                modifier = Modifier.fillMaxSize()
            ) { paddings ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            start = paddings.calculateStartPadding(layoutDirection),
                            end = paddings.calculateEndPadding(layoutDirection),
                        )
                ) {
                    AppNavHost(navigator.navController)
                }
            }
        }

    }

}