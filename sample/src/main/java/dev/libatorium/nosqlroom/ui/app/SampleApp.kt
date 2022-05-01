package dev.libatorium.nosqlroom.ui.app

import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.libatorium.lib.nosqlroom.R
import dev.libatorium.nosqlroom.domain.model.User
import dev.libatorium.nosqlroom.ui.theme.NoSqlRoomSampleTheme

@ExperimentalMaterialApi
@Composable
fun SampleApp(
    sampleAppViewModel: SampleAppViewModel
) {
    val context = LocalContext.current

    NoSqlRoomSampleTheme {
        Scaffold(topBar = {
            SampleAppBar {
                Toast.makeText(context, "Clicked!", Toast.LENGTH_SHORT).show()
                sampleAppViewModel.onAddUser()
            }
        }) { paddingValues ->
            val users by sampleAppViewModel.users.collectAsState(emptyList())
            LazyColumn(modifier = Modifier.padding(paddingValues = paddingValues)) {
                items(users) { u ->
                    UserDisplay(user = u) { user ->
                        val display = context.getString(R.string.this_is_user_id, user.id)
                        Toast.makeText(context, display, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun UserDisplay(
    user: User,
    onUserClicked: (User) -> Unit
) {
    Card(onClick = { onUserClicked(user) }, modifier = Modifier.padding(16.dp)) {
        Text(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            text = stringResource(id = R.string.user_id, formatArgs = arrayOf(user.id))
        )
    }
}

@ExperimentalMaterialApi
@Preview
@Composable
fun UserDisplayPreview() {
    UserDisplay(user = User(id = "Hello World"), onUserClicked = {})
}

@Composable
fun SampleAppBar(
    onAddClicked: () -> Unit
) {
    TopAppBar(
        title = {
            Text(text = stringResource(id = R.string.no_sql_room_sample_title))
        },
        actions = {
            IconButton(onClick = onAddClicked) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_add_black_24dp),
                    contentDescription = stringResource(id = R.string.add_item_cd),
                    tint = Color.White
                )
            }
        })
}