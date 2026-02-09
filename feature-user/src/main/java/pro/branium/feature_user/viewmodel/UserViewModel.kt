package pro.branium.feature_user.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pro.branium.feature_user.domain.model.UserModel
import pro.branium.feature_user.domain.usecase.DeleteAccountUseCase
import pro.branium.feature_user.domain.usecase.GetUserInfoUseCase
import pro.branium.feature_user.domain.usecase.LoginUseCase
import pro.branium.feature_user.domain.usecase.RegisterUseCase
import pro.branium.feature_user.domain.usecase.UpdateUserUseCase
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase,
    private val updateUserUseCase: UpdateUserUseCase,
    private val deleteAccountUseCase: DeleteAccountUseCase
) : ViewModel() {
    private val _userModel = MutableLiveData<UserModel?>()

    val mUserModel: LiveData<UserModel?> = _userModel

    init {
        getUserInfo(0)
    }

    fun getUserInfo(userId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            getUserInfoUseCase(userId).collect { user ->
                _userModel.postValue(user)
            }
        }
    }

    fun login(username: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val email = if (username.contains('@')) username else ""
            val phoneNumber = if (username.contains('@')) "" else username
            val userModel = UserModel(
                userId = 0,
                username = username,
                email = email,
                phoneNumber = phoneNumber,
                password = password
            )
            val loggedInUser = loginUseCase(userModel)
            _userModel.postValue(loggedInUser)
        }
    }

    fun register(username: String, email: String, phoneNumber: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val userModel = UserModel(
                userId = 0,
                username = username,
                email = email,
                phoneNumber = phoneNumber,
                password = password
            )
            registerUseCase(userModel)
        }
    }

    fun updateUser(
        userId: Int,
        username: String,
        email: String,
        phoneNumber: String,
        password: String,
        avatar: String?
    ) {
        val userModel = UserModel(
            userId = userId,
            username = username,
            email = email,
            phoneNumber = phoneNumber,
            password = password,
            avatar = avatar
        )
        viewModelScope.launch(Dispatchers.IO) {
            updateUserUseCase(userModel)
        }
    }

    fun deleteUser(userModel: UserModel) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteAccountUseCase(userModel)
        }
    }
}