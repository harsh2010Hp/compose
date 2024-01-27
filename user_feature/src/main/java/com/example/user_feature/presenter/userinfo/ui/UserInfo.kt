import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.domain.model.UserInfo
import com.example.user_feature.R

@Composable
fun UserInfo(userInfo: UserInfo?, modifier: Modifier = Modifier) {
    val padding = PaddingValues.medium

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(padding)
    ) {
        // User details card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(padding), elevation = Elevation.medium
        ) {
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxWidth()
            ) {
                // Name
                Text(
                    text = userInfo?.name ?: "",
                    fontSize = TextSizes.doubleExtraLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // Email
                Text(
                    text = userInfo?.email ?: "",
                    fontSize = TextSizes.medium,
                    modifier = Modifier.padding(
                        bottom = PaddingValues.small.calculateBottomPadding()
                    )
                )

                // Company info
                Column(
                    modifier = Modifier.padding(
                        top = PaddingValues.medium.calculateTopPadding()
                    ), horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = stringResource(
                            R.string.username_text, userInfo?.username ?: ""
                        ), fontSize = TextSizes.large, fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = stringResource(
                            R.string.street_text, userInfo?.address?.street ?: ""
                        ), fontSize = TextSizes.medium, maxLines = 2
                    )
                    Text(
                        text = stringResource(
                            R.string.zip_code_text, userInfo?.address?.zipcode ?: ""
                        ), fontSize = TextSizes.medium
                    )
                }
            }
        }
    }
}
