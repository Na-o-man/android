package com.hgh.na_o_man.presentation.ui.detail

import android.app.Activity
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.accompanist.pager.ExperimentalPagerApi
import com.hgh.na_o_man.presentation.ui.detail.GroupDetailFolder.GroupDetailFolderScreen
import com.hgh.na_o_man.presentation.ui.detail.agenda.AddAgendaScreen
import com.hgh.na_o_man.presentation.ui.detail.photo_list.PhotoListScreen
import com.hgh.na_o_man.presentation.ui.detail.vote.VoteMainScreen
import com.hgh.na_o_man.presentation.ui.detail.vote_detail.VoteDetailScreen

@OptIn(ExperimentalPagerApi::class)
@Composable
fun GroupDetailScreen(
    navController: NavHostController = rememberNavController(),
) {
    val context = LocalContext.current as Activity


    Log.d("리컴포저블", "GroupDetailScreen")
    Scaffold(
        containerColor = Color(0xFFBBCFE5)
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            NavHost(
                modifier = Modifier.padding(innerPadding),
                navController = navController,
                startDestination = GroupDetailScreenRoute.DETAIL.route
            ) {

                composable(route = GroupDetailScreenRoute.DETAIL.route) {
                    GroupDetailFolderScreen(
                        navigationBack = {
                            context.finish()
                        },
                        navigationPhotoList = { groupId, profileId, memberId ->
                            navController.navigate(
                                GroupDetailScreenRoute.LIST.route.plus("/${groupId}")
                                    .plus("/${profileId}").plus("/${memberId}").plus("/${false}")
                            )
                        },
                        navigationVote = { groupId ->
                            navController.navigate(
                                GroupDetailScreenRoute.VOTE.route.plus("/${groupId}")
                            )
                        },
                        navigationMyPage = { groupId ->
                            navController.navigate(
                                GroupDetailScreenRoute.AGENDA.route.plus("/${groupId}")
                            )
                        },
                    )
                }

                composable(route = GroupDetailScreenRoute.LIST.route
                    .plus("/{$KEY_GROUP_ID}")
                    .plus("/{$KEY_PROFILE_ID}")
                    .plus("/{$KEY_MEMBER_ID}")
                    .plus("/{$KEY_IS_AGENDA}"),
                    arguments = listOf(
                        navArgument(KEY_GROUP_ID) { type = NavType.LongType },
                        navArgument(KEY_PROFILE_ID) { type = NavType.LongType },
                        navArgument(KEY_MEMBER_ID) { type = NavType.LongType },
                        navArgument(KEY_IS_AGENDA) { type = NavType.BoolType }
                    )
                ) {
                    PhotoListScreen(
                        navigationBack = {
                            navController.popBackStack()
                        },
                        navigationAgenda = { photos ->
                            navController.previousBackStackEntry?.savedStateHandle?.set(
                                AGENDA_PHOTOS, photos
                            )
                            navController.popBackStack()
                        }
                    )
                }

                composable(route = GroupDetailScreenRoute.VOTE.route
                    .plus("/{$KEY_GROUP_ID}"),
                    arguments = listOf(
                        navArgument(KEY_GROUP_ID) { type = NavType.LongType }
                    )
                ) {
                    VoteMainScreen(
                        navigationBack = {
                            navController.popBackStack()
                        },
                        navigationAgenda = { groupId ->
                            navController.navigate(
                                GroupDetailScreenRoute.AGENDA.route.plus("/${groupId}")
                            )
                        },
                        navigationVoteDetail = { agendaId ->
                            navController.navigate(
                                GroupDetailScreenRoute.VOTE_DETAIL.route.plus("/${agendaId}")
                            )
                        }
                    )
                }

                composable(route = GroupDetailScreenRoute.AGENDA.route
                    .plus("/{$KEY_GROUP_ID}"),
                    arguments = listOf(
                        navArgument(KEY_GROUP_ID) { type = NavType.LongType }
                    )
                ) {
                    AddAgendaScreen(
                        navController = navController,
                        navigationBack = {
                            navController.popBackStack()
                        }, navigationPhotoList = { groupId, profileId, memberId ->
                            navController.navigate(
                                GroupDetailScreenRoute.LIST.route.plus("/${groupId}")
                                    .plus("/${profileId}").plus("/${memberId}").plus("/${true}")
                            )
                        }
                    )
                }

                composable(route = GroupDetailScreenRoute.VOTE_DETAIL.route
                    .plus("/{$KEY_AGENDA_ID}"),
                    arguments = listOf(
                        navArgument(KEY_AGENDA_ID) { type = NavType.LongType }
                    )
                ) {
                    VoteDetailScreen(
                        navigationBack = {
                            navController.popBackStack()
                        }
                    )
                }
            }
        }
    }
}

enum class GroupDetailScreenRoute(val route: String) {
    DETAIL("detail"),
    LIST("list"),
    VOTE("vote"),
    AGENDA("agenda"),
    VOTE_DETAIL("vote-detail"),
}

const val KEY_GROUP_ID = "group-id"
const val KEY_MEMBER_ID = "member-id"
const val KEY_PROFILE_ID = "profild-id"
const val KEY_AGENDA_ID = "agenda-id"
const val KEY_IS_AGENDA = "is-agenda"
const val ALL_PHOTO_ID = 100L
const val OTHER_PHOTO_ID = 101L
const val AGENDA_PHOTOS = "agenda-photos"
