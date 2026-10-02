package com.example.blogapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.blogapp.ui.blog.BlogViewModel
import com.example.blogapp.ui.blog.detail.BlogDetailScreen
import com.example.blogapp.ui.blog.edit.BlogEditScreen
import com.example.blogapp.ui.blog.list.BlogListScreen
import com.example.blogapp.ui.home.HomeScreen
import com.example.blogapp.ui.myinfo.MyInfoScreen
import com.example.blogapp.ui.myinfo.MyInfoViewModel


//각 화면을 이름으로 등록할 생각이다.
//이름 문자열이다. 화면 전환 요청하는 곳에서 문자열 오타 가능성등..
//타입 안정성을 위해서.. sealed 클래스를 이용하겠다.

//일종의 enum 사상. 내가 준비한대로만 사용하라.. 더 확장해서 만들지 마라..
//enum 보다.. 변수, 함수 추가가 편해서.. enum 이 필요한대 약간의 데이터와 로직을 유지해야 하는 경우에 주로 사용..
sealed class Screen(val route: String){
    //결국 화면의 이름을 "home" 으로 하는데.. 이것을 Home 타입으로 지정하게.. 오타가 허용되지 않는다.
    object Home: Screen("home")
    object MyInfo: Screen("my_info")

    object BlogGraph: Screen("blog_graph")//임의 이름.. 이 이름의 스택 정보가 유지되게..
    //유저 입장의 새로운 화면은 아니다.. 단지.. 관련있는 화면들을 묶는 역할.. 이곳에 ViewModel 유지해서 관련된 화면 내에서만 이용되게

    object BlogList: Screen("blog_list")
    object BlogEdit: Screen("blog_edit?postId={postId}") {
        //아규먼트 포함..
        //개발자 임의 함수이다. 화면전환 요청 문자열 쉽게 만들라고..
        fun createRoute(postId: Long? = null) =
            if (postId != null) "blog_edit?postId=$postId" else "blog_edit"
    }
    object BlogDetail: Screen("blog_detail/{postId}"){
        fun createRoute(postId: Long) = "blog_detail/$postId"
    }
}

@Composable
//여러 컴포저블에서 뷰 모델 준비의 동일 코드가 있어서.  중복을 피하기 위해서..
//@Composable 으로 선언하는 이유는 @Composable에서만 호출가능한 함수가 있엇..
private fun rememberBlogViewModel(
    navController: NavController,
    backStackEntry: NavBackStackEntry
): BlogViewModel {
    //back stack 에 유지되고 있는 Screen.BlogGraph.route 이름의 BackStackEntry 객체를 획득..
    //획득한 BackStackEntry 에 뷰모델을 저장해서 .해당 BackStackEntry 이 사라질때 뷰모델도 제거하려고..
    val parentEntry = remember(backStackEntry){
        navController.getBackStackEntry(Screen.BlogGraph.route)
    }
    //매개변수의 BackStackEntry 에 뷰모델이 저장된다..
    return hiltViewModel(parentEntry)
}




@Composable
fun BlogNavHost(navController: NavHostController = rememberNavController()) {

    //개별 composable 에서 직접 viewmodel 획득 이용도 가능하지만..
    //navigation 에서 획득해서 전달해 주겠다..
    //viewmodel 의 이용 범위를 한정짓기 위해서..
    //앱의 생존주기와 동일..
    val myInfoViewModel: MyInfoViewModel = hiltViewModel()
//    val blogViewModel: BlogViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ){
        //화면 등록..
        composable(Screen.Home.route){
            HomeScreen(
                viewModel = myInfoViewModel,
                onNavigateToMyInfo = { navController.navigate(Screen.MyInfo.route)},
                onNavigateToBlogList = { navController.navigate(Screen.BlogList.route)}
            )
        }
        composable(Screen.MyInfo.route){
            MyInfoScreen(
                viewModel = myInfoViewModel,
                onBack = { navController.popBackStack() }
            )
        }

        //navigation 안쓸때..
        //[home -> list]
        //navigation 이용
        //[home -> graph -> list]
        navigation(//가상의 화면.. 유저에 출력되지는 않지만.. backstack 에는 정보로 들어간다..

            startDestination = Screen.BlogList.route,
            route = Screen.BlogGraph.route
        ){
            //NavHostController 를 각 컴포저블에 넘겨서 컴포저블에서 직접 navigate() 함수 호출로 화면 전환을 할 수도 있지만..
            composable(Screen.BlogList.route){
                val blogViewModel = rememberBlogViewModel(navController, it)
                BlogListScreen(
                    viewModel = blogViewModel,
                    onNavigateToBlogDetail = { postId ->
                        navController.navigate(Screen.BlogDetail.createRoute(postId))
                    },
                    onNavigateToBlogEdit = {
                        navController.navigate(Screen.BlogEdit.createRoute())
                    },
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }
            composable(
                route = Screen.BlogEdit.route,
                arguments = listOf(//전달 받아야 하는 데이터 명시..
                    navArgument("postId"){
                        type = NavType.LongType
                        defaultValue = -1L//optional
                    }
                )
            ){ backStackEntry -> //BackStackEntry, 실제 화면 전환시에 NavHost에서 백스택에 유지하는 객체.. 이 객체에 화면 전환과 관련된
                //모든 정보, 어느 이름의 화면이고, 그때 넘기는 데이터가 무엇이고 등등이 유지된다.
                //전달된 데이터 획득..
                val blogViewModel = rememberBlogViewModel(navController, backStackEntry)
                val postId = backStackEntry.arguments?.getLong("postId") ?: -1L
                BlogEditScreen(
                    viewModel = blogViewModel,
                    postId = if(postId == -1L) null else postId,
                    onBack = { navController.popBackStack() },
                    onSaved = { navController.popBackStack() }
                )
            }
            composable(
                route = Screen.BlogDetail.route,
                arguments = listOf(
                    navArgument("postId"){
                        type = NavType.LongType
                    }
                )
            ) { backStackEntry ->
                val postId = backStackEntry.arguments?.getLong("postId") ?: return@composable
                val blogViewModel = rememberBlogViewModel(navController, backStackEntry)
                BlogDetailScreen(
                    viewModel = blogViewModel,
                    postId = postId,
                    onBack = { navController.popBackStack() },
                    onEdit = { navController.navigate(Screen.BlogEdit.createRoute(postId))},
                    onDelete = { navController.popBackStack() }
                )
            }
        }


    }
}

















