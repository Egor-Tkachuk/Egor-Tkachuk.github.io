import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import dailyrunnerweb.composeapp.generated.resources.Res
import dailyrunnerweb.composeapp.generated.resources.lato_bold
import dailyrunnerweb.composeapp.generated.resources.lato_italic
import dailyrunnerweb.composeapp.generated.resources.lato_light
import dailyrunnerweb.composeapp.generated.resources.lato_regular
import dailyrunnerweb.composeapp.generated.resources.poppins_bold
import dailyrunnerweb.composeapp.generated.resources.poppins_regular
import org.jetbrains.compose.resources.Font

@Composable
fun lato() = FontFamily(
    Font(Res.font.lato_regular,
        weight = FontWeight.Normal,
        style = FontStyle.Normal),
    Font(Res.font.lato_italic,
        weight = FontWeight.Normal,
        style = FontStyle.Italic),
    Font(Res.font.lato_bold,
        weight = FontWeight.Bold,
        style = FontStyle.Normal),
    Font(Res.font.lato_light,
        weight = FontWeight.Light,
        style = FontStyle.Normal)
)

@Composable
fun poppins() = FontFamily(
    listOf(
        Font(Res.font.poppins_regular,
            weight = FontWeight.Normal,
            style = FontStyle.Normal),
        Font(Res.font.poppins_bold,
            weight = FontWeight.Bold,
            style = FontStyle.Normal)
    )
)