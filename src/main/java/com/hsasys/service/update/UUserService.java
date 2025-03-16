package com.hsasys.service.update;


import com.hsasys.domain.dto.UserUpdateDto;
import com.hsasys.domain.entity.*;
import com.hsasys.result.Result;

import java.util.List;

public interface UUserService {

    Result updateUserInfo(User user);

    Result updateUser(UserUpdateDto userUpdateDto);

    Result<List<Allergen>> getAllergens();

    Result<List<ChronicDisease>> getDiseases();

    Result<List<FoodPreference>> getPreferences();

    Result<UserType> selectType();

    Result<Integer> getScore(Long memberId);
}
