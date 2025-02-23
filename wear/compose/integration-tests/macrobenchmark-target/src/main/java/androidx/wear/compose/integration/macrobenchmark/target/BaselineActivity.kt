/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package androidx.wear.compose.integration.macrobenchmark.target

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.wear.compose.foundation.CurvedLayout
import androidx.wear.compose.foundation.CurvedModifier
import androidx.wear.compose.foundation.CurvedTextStyle
import androidx.wear.compose.foundation.basicCurvedText
import androidx.wear.compose.foundation.padding
import androidx.wear.compose.material.AppCard
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.Card
import androidx.wear.compose.material.Checkbox
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ChipDefaults
import androidx.wear.compose.material.CircularProgressIndicator
import androidx.wear.compose.material.CompactButton
import androidx.wear.compose.material.CompactChip
import androidx.wear.compose.material.ExperimentalWearMaterialApi
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.InlineSlider
import androidx.wear.compose.material.InlineSliderDefaults
import androidx.wear.compose.material.ListHeader
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.OutlinedButton
import androidx.wear.compose.material.OutlinedChip
import androidx.wear.compose.material.OutlinedCompactButton
import androidx.wear.compose.material.OutlinedCompactChip
import androidx.wear.compose.material.Picker
import androidx.wear.compose.material.PlaceholderDefaults
import androidx.wear.compose.material.PositionIndicator
import androidx.wear.compose.material.RadioButton
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.SplitToggleChip
import androidx.wear.compose.material.Stepper
import androidx.wear.compose.material.StepperDefaults
import androidx.wear.compose.material.Switch
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import androidx.wear.compose.material.TitleCard
import androidx.wear.compose.material.ToggleButton
import androidx.wear.compose.material.ToggleChip
import androidx.wear.compose.material.ToggleChipDefaults
import androidx.wear.compose.material.Vignette
import androidx.wear.compose.material.VignettePosition
import androidx.wear.compose.material.curvedText
import androidx.wear.compose.material.dialog.Alert
import androidx.wear.compose.material.dialog.Confirmation
import androidx.wear.compose.material.placeholder
import androidx.wear.compose.material.placeholderShimmer
import androidx.wear.compose.material.rememberPickerState
import androidx.wear.compose.material.rememberPlaceholderState
import androidx.wear.compose.material.scrollAway
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import kotlinx.coroutines.delay

private val ALERT_DIALOG = "alert-dialog"
private val CONFIRMATION_DIALOG = "confirmation-dialog"
private val BUTTONS = "buttons"
private val CARDS = "cards"
private val CHIPS = "chips"
private val RADIO_BUTTON = "radio-button"
private val CHECKBOX = "checkbox"
private val SWITCH = "switch"
private val DIALOGS = "dialogs"
private val PICKER = "picker"
private val PROGRESSINDICATORS = "progressindicators"
private val SLIDER = "slider"
private val START_INDEX = "start-index"
private val STEPPER = "stepper"
private val SWIPE_DISMISS = "swipe-dismiss"
private val PROGRESS_INDICATOR = "progress-indicator"
private val PROGRESS_INDICATOR_INDETERMINATE = "progress-indicator-indeterminate"
private val PLACEHOLDERS = "placeholders"

class BaselineActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberSwipeDismissableNavController()
            val scrollState = rememberScrollState()

            MaterialTheme {
                Scaffold(
                    timeText = {
                        TimeText(
                            modifier = Modifier.scrollAway(scrollState = scrollState)
                        )
                    },
                    positionIndicator = { PositionIndicator(scrollState = scrollState) },
                    vignette = { Vignette(vignettePosition = VignettePosition.TopAndBottom) },
                ) {
                    SwipeDismissableNavHost(
                        navController = navController,
                        startDestination = START_INDEX,
                        modifier = Modifier
                            .background(MaterialTheme.colors.background)
                            .semantics { contentDescription = SWIPE_DISMISS }
                    ) {
                        composable(START_INDEX) { StartIndex(navController, scrollState) }
                        composable(DIALOGS) {
                            Dialogs(navController)
                        }
                        composable(ALERT_DIALOG) {
                            Alert(
                                title = { Text("Alert") },
                                negativeButton = {},
                                positiveButton = {},
                            )
                        }
                        composable(CONFIRMATION_DIALOG) {
                            Confirmation(
                                onTimeout = { navController.popBackStack() },
                                content = { Text("Confirmation") },
                            )
                        }
                        composable(BUTTONS) { Buttons() }
                        composable(CARDS) { Cards() }
                        composable(CHIPS) { Chips() }
                        composable(PICKER) { Picker(scrollState) }
                        composable(PLACEHOLDERS) { Placeholders() }
                        composable(PROGRESSINDICATORS) { ProgressIndicators(navController) }
                        composable(PROGRESS_INDICATOR) {
                            CircularProgressIndicator(
                                modifier = Modifier.fillMaxSize(),
                                startAngle = 300f,
                                endAngle = 240f,
                                progress = 0.3f
                            )
                        }
                        composable(PROGRESS_INDICATOR_INDETERMINATE) {
                            Column(
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.fillMaxSize(),
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                        composable(SLIDER) { Slider() }
                        composable(STEPPER) { Stepper() }
                    }
                }
            }
        }
    }
}

@Composable
fun StartIndex(navController: NavHostController, scrollState: ScrollState) {
    Box {
        CurvedTexts()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(state = scrollState)
                .padding(vertical = 32.dp)
                .semantics { contentDescription = CONTENT_DESCRIPTION },
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Widget(navController, BUTTONS, "Btn", BUTTONS)
                Widget(navController, CARDS, "Card", CARDS)
                Widget(navController, PLACEHOLDERS, "Plc", PLACEHOLDERS)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Widget(navController, CHIPS, "Chip", CHIPS)
                Widget(navController, DIALOGS, "Dlg", DIALOGS)
                Widget(navController, PICKER, "Pick", PICKER)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Widget(navController, PROGRESSINDICATORS,
                    "Prog", PROGRESSINDICATORS)
                Widget(navController, SLIDER, "Sldr", SLIDER)
                Widget(navController, STEPPER, "Stpr", STEPPER)
            }
        }
    }
}

@Composable
fun Dialogs(navController: NavHostController) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ListHeader { Text("Dialogs") }
        CompactChip(
            onClick = { navController.navigate(ALERT_DIALOG) },
            colors = ChipDefaults.primaryChipColors(),
            label = { Text(ALERT_DIALOG) },
            modifier = Modifier.semantics {
                contentDescription = ALERT_DIALOG
            },
        )
        Spacer(Modifier.height(4.dp))
        CompactChip(
            onClick = { navController.navigate(CONFIRMATION_DIALOG) },
            colors = ChipDefaults.primaryChipColors(),
            label = { Text(CONFIRMATION_DIALOG) },
            modifier = Modifier.semantics {
                contentDescription = CONFIRMATION_DIALOG
            },
        )
    }
}

@Composable
fun Buttons() {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        ListHeader { Text("Buttons") }
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            Button(onClick = {}) { Text("B") }
            OutlinedButton(onClick = {}) { Text("OB") }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            CompactButton(onClick = {}) { Text("CB") }
            OutlinedCompactButton(onClick = {}) { Text("OCB") }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            ToggleButton(
                checked = true,
                onCheckedChange = {}) { Text("TB") }
        }
    }
}

@Composable
fun Cards() {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ListHeader { Text("Cards") }
        Card(onClick = {}) { Text("Card") }
        AppCard(onClick = {},
            appName = { Text("AppName") }, title = {},
            time = { Text("02:34") }) {
            Text("AppCard")
        }
        TitleCard(onClick = {}, title = { Text("Title") }) {
            Text("TitleCard")
        }
    }
}

@Composable
fun Chips() {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            Chip(
                modifier = Modifier.height(32.dp),
                onClick = {},
                colors = ChipDefaults.primaryChipColors(),
                label = { Text("C") }
            )
            OutlinedChip(
                modifier = Modifier.height(32.dp),
                onClick = {},
                label = { Text("OC") }
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            CompactChip(onClick = {}, label = { Text("CC") })
            OutlinedCompactChip(onClick = {}, label = { Text("OCC") })
        }
        Row(horizontalArrangement = Arrangement.SpaceEvenly) {
            var radioState by remember { mutableStateOf(false) }
            ToggleChip(
                checked = radioState,
                onCheckedChange = { radioState = !radioState },
                label = { Text("R") },
                toggleControl = {
                    Icon(
                        imageVector =
                        ToggleChipDefaults.radioIcon(checked = radioState),
                        contentDescription = null
                    )
                }
            )
            var switchState by remember { mutableStateOf(false) }
            ToggleChip(
                checked = switchState,
                onCheckedChange = { switchState = !switchState },
                label = { Text("S") },
                toggleControl = {
                    Icon(
                        imageVector =
                        ToggleChipDefaults.switchIcon(checked = switchState),
                        contentDescription = null
                    )
                }
            )
            var checkboxState by remember { mutableStateOf(false) }
            ToggleChip(
                checked = checkboxState,
                onCheckedChange = { checkboxState = !checkboxState },
                label = { Text("C") },
                toggleControl = {
                    Icon(
                        imageVector =
                        ToggleChipDefaults.checkboxIcon(checked = checkboxState),
                        contentDescription = null
                    )
                }
            )
        }
        Row(horizontalArrangement = Arrangement.SpaceEvenly) {
            var radioState by remember { mutableStateOf(false) }
            ToggleChip(
                checked = radioState,
                onCheckedChange = { radioState = !radioState },
                label = { Text("R") },
                toggleControl = { RadioButton(selected = radioState) },
                modifier = Modifier.semantics { contentDescription = RADIO_BUTTON },
            )
            var switchState by remember { mutableStateOf(false) }
            ToggleChip(
                checked = switchState,
                onCheckedChange = { switchState = !switchState },
                label = { Text("S") },
                toggleControl = { Switch(checked = switchState) },
                modifier = Modifier.semantics { contentDescription = SWITCH },
            )
            var checkboxState by remember { mutableStateOf(false) }
            ToggleChip(
                checked = checkboxState,
                onCheckedChange = { checkboxState = !checkboxState },
                label = { Text("C") },
                toggleControl = { Checkbox(checked = checkboxState) },
                modifier = Modifier.semantics { contentDescription = CHECKBOX },
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            SplitToggleChip(
                checked = true,
                onCheckedChange = {},
                label = { Text("Split") },
                onClick = {},
                toggleControl = {
                    Icon(
                        imageVector =
                        ToggleChipDefaults.radioIcon(checked = true),
                        contentDescription = null
                    )
                }
            )
        }
    }
}

@Composable
fun Picker(scrollState: ScrollState) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(state = scrollState)
            .padding(vertical = 16.dp)
            .semantics { contentDescription = CONTENT_DESCRIPTION },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ListHeader { Text("Pickers") }
        val items = listOf("One", "Two", "Three", "Four", "Five")
        val state = rememberPickerState(items.size)
        val contentDescription by remember {
            derivedStateOf { "${state.selectedOption + 1 }" }
        }
        Picker(
            state = state,
            contentDescription = contentDescription,
            option = { Text(items[it]) },
            modifier = Modifier.size(100.dp, 100.dp),
        )
    }
}

@OptIn(ExperimentalWearMaterialApi::class)
@Composable
fun Placeholders() {
    var labelText by remember { mutableStateOf("") }
    var iconContent: @Composable () -> Unit = { Checkbox(true) }
    val chipPlaceholderState = rememberPlaceholderState {
        labelText.isNotEmpty()
    }

    Chip(
        onClick = { /* Do something */ },
        enabled = true,
        label = {
            Text(
                text = labelText,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth()
                    .placeholder(chipPlaceholderState)
            )
        },
        icon = {
            Box(
                modifier = Modifier
                    .size(ChipDefaults.IconSize)
                    .placeholder(chipPlaceholderState),
            ) {
                iconContent()
            }
        },
        colors = PlaceholderDefaults.placeholderChipColors(
            originalChipColors = ChipDefaults.primaryChipColors(),
            placeholderState = chipPlaceholderState
        ),
        modifier = Modifier
            .fillMaxWidth()
            .placeholderShimmer(chipPlaceholderState)
    )
    LaunchedEffect(Unit) {
        delay(50)
        iconContent = { Switch(true) }
        delay(1000)
        labelText = "A label"
    }
    if (!chipPlaceholderState.isShowContent) {
        LaunchedEffect(chipPlaceholderState) {
            chipPlaceholderState.startPlaceholderAnimation()
        }
    }
}

@Composable
fun ProgressIndicators(navController: NavHostController) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ListHeader { Text("Progress Indicators") }
        // Test both circular progress indicator with gap and spinning indicator.
        CompactChip(
            onClick = { navController.navigate(PROGRESS_INDICATOR) },
            colors = ChipDefaults.primaryChipColors(),
            label = { Text(PROGRESS_INDICATOR) },
            modifier = Modifier.semantics {
                contentDescription = PROGRESS_INDICATOR
            },
        )
        Spacer(Modifier.height(4.dp))
        CompactChip(
            onClick = {
                navController.navigate(
                    PROGRESS_INDICATOR_INDETERMINATE
                )
            },
            colors = ChipDefaults.primaryChipColors(),
            label = { Text(PROGRESS_INDICATOR_INDETERMINATE) },
            modifier = Modifier.semantics {
                contentDescription = PROGRESS_INDICATOR_INDETERMINATE
            },
        )
    }
}

@Composable
fun Slider() {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ListHeader { Text("Sliders") }
        var value by remember { mutableFloatStateOf(4.5f) }
        InlineSlider(
            value = value,
            onValueChange = { value = it },
            increaseIcon = {
                Icon(
                    InlineSliderDefaults.Increase,
                    "Increase"
                )
            },
            decreaseIcon = {
                Icon(
                    InlineSliderDefaults.Decrease,
                    "Decrease"
                )
            },
            valueRange = 3f..6f,
            steps = 5,
            segmented = false
        )
    }
}

@Composable
fun Stepper() {
    var value by remember { mutableFloatStateOf(2f) }
    Stepper(
        value = value,
        onValueChange = { value = it },
        increaseIcon = { Icon(StepperDefaults.Increase, "Increase") },
        decreaseIcon = { Icon(StepperDefaults.Decrease, "Decrease") },
        valueRange = 1f..4f,
        steps = 7
    ) { Text("Value: $value") }
}

@Composable
fun CurvedTexts() {
    val background = MaterialTheme.colors.background
    CurvedLayout(anchor = 235f) {
        basicCurvedText(
            "Basic",
            CurvedTextStyle(
                fontSize = 16.sp,
                color = Color.White,
                background = background
            ),
            modifier = CurvedModifier.padding(2.dp)
        )
    }
    CurvedLayout(anchor = 310f) {
        curvedText(text = "Curved")
    }
}

@Composable
fun Widget(navController: NavHostController, destination: String, text: String, desc: String) {
    Button(
        onClick = { navController.navigate(destination) },
        modifier = Modifier.semantics { contentDescription = desc }
    ) {
        Text(text)
    }
}
