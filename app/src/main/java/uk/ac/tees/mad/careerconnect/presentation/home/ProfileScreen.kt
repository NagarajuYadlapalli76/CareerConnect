package uk.ac.tees.mad.careerconnect.presentation.home


import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.MediaStore

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter


import uk.ac.tees.mad.careerconnect.R
import uk.ac.tees.mad.careerconnect.data.remote.uriToByteArray
import uk.ac.tees.mad.careerconnect.presentation.auth.AuthViewModel


import androidx.compose.runtime.*
import coil3.compose.rememberAsyncImagePainter
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.crossfade

@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun ProfilePage(
    authViewModel: AuthViewModel,
) {
    val context = LocalContext.current
    var update by remember { mutableStateOf(false) }


    val currentUser = authViewModel.currentUserData.collectAsState().value


    var newMobile by rememberSaveable { mutableStateOf("") }
    var newName by rememberSaveable { mutableStateOf("") }
    var isLoading by rememberSaveable { mutableStateOf(false) }
    var pf by rememberSaveable { mutableStateOf("") }
    val cornerShape = RoundedCornerShape(14.dp)
    var isEditing by rememberSaveable { mutableStateOf(false) }
    var showError by rememberSaveable { mutableStateOf(false) }

    val scope = rememberCoroutineScope()


    val freshUrl = "${currentUser.profileImageUrl}?t=${System.currentTimeMillis()}"

    val imageRequest = ImageRequest.Builder(context)
        .data(freshUrl)
        .crossfade(true)
        .diskCachePolicy(CachePolicy.ENABLED)
        .memoryCachePolicy(CachePolicy.ENABLED)
        .build()


    val painter = rememberAsyncImagePainter(model = imageRequest)

// Access state directly (no collectAsState needed)
    val state by painter.state.collectAsState()

    // PDF Selector
    var selectedPDFUri by remember { mutableStateOf<Uri?>(null) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val pdfLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedPDFUri = uri
    }
    val defaultPdfUri = Uri.parse(
        "${ContentResolver.SCHEME_ANDROID_RESOURCE}://${context.packageName}/${R.drawable.default_resume}"
    )
    val defaulImagetUri = Uri.parse(
        "${ContentResolver.SCHEME_ANDROID_RESOURCE}://${context.packageName}/${R.drawable.default_profile}"
    )

    val pdfUri: Uri = if (selectedPDFUri == null) {
        defaultPdfUri
    } else {
        selectedPDFUri!!
    }

    val imageUri: Uri = if (selectedImageUri == null) {
        defaulImagetUri
    } else {
        selectedImageUri!!
    }

//android 13
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(), onResult = { uri ->
            if (uri != null) {
                selectedImageUri = uri
            } else {
                Toast.makeText(context, "No image selected", Toast.LENGTH_SHORT).show()
            }
        })

//android 12
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(), onResult = { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val uri = result.data?.data
                selectedImageUri = uri
            } else {
                Toast.makeText(context, "No image selected", Toast.LENGTH_SHORT).show()
            }
        })

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Profile", color = MaterialTheme.colorScheme.onBackground, fontSize = 22.sp
                    )
                }, colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF3B6CFF))
            )
        }) { innerPadding ->
        Spacer(modifier = Modifier.height(25.dp))
        Box(modifier = Modifier.padding(innerPadding)) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Profile Image
                Box(contentAlignment = Alignment.BottomEnd) {

                    if (selectedImageUri != null) {

                        AsyncImage(
                            model = selectedImageUri,
                            contentDescription = "Profile Image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape)
                        )
                    } else if (state is AsyncImagePainter.State.Loading) {
                        Box(
                            contentAlignment = Alignment.Center, modifier = Modifier.size(100.dp)
                        ) {
                            AsyncImage(
                                model = R.drawable.pf,
                                contentDescription = "Profile Image",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(100.dp)
                                    .clip(CircleShape)
                            )


                            CircularProgressIndicator(
                                color = Color.Black,

                                strokeWidth = 2.dp, modifier = Modifier.size(30.dp)

                            )
                        }


                    } else {

                        AsyncImage(
                            model = imageRequest,
                            contentDescription = "Profile Image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape)
                        )
                    }


                    if (isEditing) {
                        IconButton(
                            onClick = {

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                    photoPickerLauncher.launch(
                                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                    )
                                } else {
                                    val intent = Intent(
                                        Intent.ACTION_PICK,
                                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                                    )
                                    galleryLauncher.launch(intent)
                                }
                            }, modifier = Modifier
                                .size(35.dp)
                                .background(
                                    color = Color(0xFF5D87FF), CircleShape
                                )
                                .size(30.dp)

                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit Image",
                                tint = MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                }
                Spacer(modifier = Modifier.height(16.dp))



                if (isEditing == false) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp, horizontal = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Edit your profile",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Medium
                            ),
                            color = MaterialTheme.colorScheme.onBackground
                        )

                        IconButton(onClick = {
                            isEditing = true
                        }) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit Profile",
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }
                }

                // Editable Text Fields
                OutlinedTextField(
                    value = if (isEditing) newName else currentUser.name,
                    enabled = isEditing,
                    onValueChange = { input ->
                        newName = input.split(" ").joinToString(" ") { word ->
                            if (word.isNotEmpty()) word.replaceFirstChar { it.uppercase() }
                            else word
                        }
                    },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = cornerShape
                )



                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = if (isEditing) newMobile else currentUser.mobNumber,
                    enabled = isEditing,
                    onValueChange = {
                        newMobile = it
                        showError = it.length != 10 // validation check
                    },
                    label = {
                        Text(
                            if (currentUser.mobNumber.isEmpty()) "Add mobile number" else "Update mobile"
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    shape = cornerShape,
                    isError = showError
                )



                Spacer(modifier = Modifier.height(16.dp))



                TextButton(onClick = {

                    if (currentUser.resumePddUrl.isEmpty()) {
                        isEditing = !isEditing
                    } else {
                        val intent = Intent(Intent.ACTION_VIEW).apply {
                            setDataAndType(Uri.parse(currentUser.resumePddUrl), "application/pdf")
                        }
                        val chooser = Intent.createChooser(intent, "Open PDF with")
                        context.startActivity(chooser)
                    }


                }) {
                    Text(

                        text = if (currentUser.resumePddUrl.isEmpty()) "Error: No resume selected. Please edit your profile." else
                            "View Your Resume",
                        color = if (currentUser.resumePddUrl.isEmpty()) Color.Blue else
                            Color.LightGray,
                        fontSize = 14.sp
                    )
                }



                Spacer(modifier = Modifier.height(16.dp))
                if (isEditing) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // PDF Selector as modern card-style button
                        TextButton(
                            onClick = { pdfLauncher.launch("application/pdf") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .border(
                                    BorderStroke(1.dp, Color.Gray),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .padding(horizontal = 16.dp),
                        ) {
                            Text(
                                text = selectedPDFUri?.lastPathSegment ?: "Select Resume PDF",
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        Spacer(modifier = Modifier.height(48.dp))

                        // Action Buttons Row
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            OutlinedButton(
                                onClick = { isEditing = false },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp),
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground)
                            ) {
                                Text("Cancel", color = MaterialTheme.colorScheme.onBackground)
                            }
                            Spacer(modifier = Modifier.weight(0.1f))
                            Button(


                                onClick = {


                                    isLoading = true
                                    val profielImageByteArray =
                                        imageUri.uriToByteArray(context)
                                    val resumePdfByteArray = pdfUri.uriToByteArray(context)


                                    profielImageByteArray?.let() {
                                        resumePdfByteArray?.let {
                                            authViewModel.updateProfile(
                                                ProfielImageByteArray = profielImageByteArray,
                                                ResumePdfByteArray = resumePdfByteArray,
                                                name = if (newName.isNotBlank()) newName else currentUser.name,
                                                mobNumber = if (newName.isNotBlank()) newMobile else currentUser.mobNumber,
                                                onResult = { message, boolean ->
                                                    if (boolean) {
                                                        Toast.makeText(
                                                            context,
                                                            message,
                                                            Toast.LENGTH_SHORT
                                                        ).show()

                                                        isEditing = false

                                                    } else {
                                                        isLoading = false

                                                        Toast.makeText(
                                                            context,
                                                            message,
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                    }

                                                })
                                        }
                                    }

                                    update = !update


                                },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(
                                        0xFF3B6CFF
                                    )
                                )
                            ) {
                                if (isLoading) {
                                    CircularProgressIndicator(
                                        color = MaterialTheme.colorScheme.onBackground,
                                        strokeWidth = 2.dp,
                                        modifier = Modifier.size(20.dp)
                                    )
                                } else {
                                    Text(
                                        "Update",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onBackground,
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.weight(1f)) // pushes the button to bottom
                if (isEditing == false) {
                    OutlinedButton(
                        onClick = { authViewModel.logoutUser() },
                        modifier = Modifier,
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red),
                        border = BorderStroke(2.dp, Color.Red)
                    ) {
                        Text(
                            text = "Log Out", fontSize = 18.sp, color = Color.Red
                        )
                    }


                    Spacer(modifier = Modifier.height(80.dp))
                }

            }

        }


    }

}










