package com.hsasys.mapper;

import com.hsasys.domain.entity.Family;
import com.hsasys.domain.entity.FamilyMember;
import com.hsasys.domain.entity.FamilyRole;
import com.hsasys.domain.vo.FamilyMemberVo;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FamilyMapper
{

    /**
     * 创建家庭
     * @param family
     */
    @Insert("insert into family (family_name, own_id, create_at, family_size) values (#{familyName}, #{ownId}, #{createAt}, #{familySize})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void createFamily(Family family);

    /**
     * 添加家庭成员
     * @param familyMember
     */
    @Insert("insert into family_member (family_id, user_id, joined_at) values (#{familyId}, #{userId}, #{joinedAt})")
    void addFamilyMember(FamilyMember familyMember);

    /**
     * 添加家庭角色
     * @param userId
     * @param roleId
     */
    @Insert("insert into family_role_user (user_id, role_id) values (#{userId}, #{roleId})")
    void addFamilyRole(@Param("userId") Long userId, @Param("roleId") int roleId);

    /**
     * 查看自己在家庭的角色
     * @param userId
     * @return
     */
    FamilyRole selectFamilyRoleByUserId(Long userId);

    /**
     * 查看家庭
     * @param userId
     * @return
     */
    Family selectFamilyByUserId(Long userId);

    /**
     * 查看家庭成员id
     * @param familyId
     * @return
     */
    @Select("select user_id from family_member where family_id = #{familyId}")
    List<Integer> selectFamilyMemberByFamilyId(Long familyId);

    @Delete("delete from family_member where family_id = #{familyId}")
    void deleteFamilyMemberByFamilyId(Long familyId);

    @Delete("delete from family where id = #{familyId}")
    void deleteFamilyByFamilyId(Long familyId);

    void deleteFamilyRoleByUserIds(@Param("userIds") List<Integer> userIds);

    @Delete("delete from family_member where user_id = #{id}")
    void deleteFamilyMemberByUserId(Long id);

    @Delete("delete from family_role_user where user_id = #{id}")
    void deleteFamilyRoleByUserId(Long id);

    void updateFamily(Family family);
}
