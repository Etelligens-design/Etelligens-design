package com.pch.ete.retrofit;

import com.pch.ete.base.BaseResponse;
import com.pch.ete.ui.incoming_request.model.IncomingRequestInfoRes;
import com.pch.ete.ui.login.model.LoginDataRes;
import com.pch.ete.ui.my_competence.model.CompetenceInfoRes;
import com.pch.ete.ui.my_equipment.model.EquipmentInfoRes;
import com.pch.ete.ui.my_technician.model.SkillDataRes;
import com.pch.ete.ui.my_technician.model.TechnicianInfoRes;
import com.pch.ete.ui.notification.model.NotificationInfoRes;
import com.pch.ete.ui.profile.model.MakeModelSkillRes;
import com.pch.ete.ui.profile.model.UserSkillDataRes;
import com.pch.ete.ui.service_request.model.ServiceRequestDataRes;
import com.pch.ete.ui.service_request.model.ServiceTypeDataRes;
import com.pch.ete.ui.technical_note.model.MyTechNoteData;
import com.pch.ete.ui.technical_note.model.MyTechNoteDataRes;
import com.pch.ete.ui.technical_video.model.MyTechVideoDataRes;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

interface APIService {
    @FormUrlEncoded
    @POST("auth/register")
    Call<LoginDataRes> register(
            @Field("user_type") int userType,
            @Field("user_name") String userName,
            @Field("full_name") String fullName,
            @Field("email") String email,
            @Field("phone") String phone,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("auth/login")
    Call<LoginDataRes> login(
            @Field("email") String email,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("auth/login_with_token")
    Call<LoginDataRes> loginWithToken(
            @Header("Authorization") String token,
            @Field("info") String info
    );

    @FormUrlEncoded
    @POST("update_device_token")
    Call<BaseResponse> updateDeviceToken(
            @Header("Authorization") String token,
            @Field("device_token") String deviceToken
    );

    @FormUrlEncoded
    @POST("update_user_name")
    Call<BaseResponse> updateUserName(
            @Header("Authorization") String token,
            @Field("user_name") String userName
    );

    @FormUrlEncoded
    @POST("update_role")
    Call<BaseResponse> updateUserRole(
            @Header("Authorization") String token,
            @Field("role") String role
    );

    @FormUrlEncoded
    @POST("update_full_name")
    Call<BaseResponse> updateFullName(
            @Header("Authorization") String token,
            @Field("full_name") String fullName
    );

    @FormUrlEncoded
    @POST("update_description")
    Call<BaseResponse> updateUserDescription(
            @Header("Authorization") String token,
            @Field("description") String description
    );

    @Multipart
    @POST("update_profile_image")
    Call<BaseResponse> updateProfileImage(
            @Header("Authorization") String token,
            @Part MultipartBody.Part profileLogo
    );

//    @FormUrlEncoded
//    @POST("update_email")
//    Call<BaseResponse> updateEmail(
//            @Header("Authorization") String token,
//            @Field("email") String email
//    );

    @FormUrlEncoded
    @POST("update_phone")
    Call<BaseResponse> updatePhone(
            @Header("Authorization") String token,
            @Field("phone") String phone
    );


    @FormUrlEncoded
    @POST("get_make_model_skill")
    Call<MakeModelSkillRes> getMakeModelSkillList(
            @Header("Authorization") String token,
            @Field("user_id") String userId
    );

    @Multipart
    @POST("add_skill")
    Call<UserSkillDataRes> addSkill(
            @Header("Authorization") String token,
            @Part("make_id") RequestBody makeId,
            @Part("model_id") RequestBody modelId,
            @Part("skill_id") RequestBody skillId,
            @Part MultipartBody.Part certificatedDoc,
            @Part MultipartBody.Part authDoc
    );

    @Multipart
    @POST("add_competence")
    Call<CompetenceInfoRes> addCompetence(
            @Header("Authorization") String token,
            @Part("make_id") RequestBody makeId,
            @Part("model_id") RequestBody modelId,
            @Part("skill_id") RequestBody skillId,
            @Part("service_hour") RequestBody serviceHour,
            @Part MultipartBody.Part certificatedDoc,
            @Part MultipartBody.Part authDoc
    );

    @Multipart
    @POST("update_competence")
    Call<CompetenceInfoRes> updateCompetence(
            @Header("Authorization") String token,
            @Part("competence_id") RequestBody competenceId,
            @Part("make_id") RequestBody makeId,
            @Part("model_id") RequestBody modelId,
            @Part("skill_id") RequestBody skillId,
            @Part("service_hour") RequestBody serviceHour,
            @Part MultipartBody.Part certificatedDoc,
            @Part MultipartBody.Part authDoc
    );

    @FormUrlEncoded
    @POST("get_user_skills")
    Call<UserSkillDataRes> getUserSkills(
            @Header("Authorization") String token,
            @Field("user_id") String userId
    );

    @FormUrlEncoded
    @POST("get_other_user_skills")
    Call<UserSkillDataRes> getOtherUserSkills(
            @Header("Authorization") String token,
            @Field("user_id") String userId
    );

    @FormUrlEncoded
    @POST("get_skill_list")
    Call<SkillDataRes> getSkillList(
            @Header("Authorization") String token,
            @Field("user_id") String userId
    );

    @FormUrlEncoded
    @POST("get_token")
    Call<BaseResponse> getToken(
            @Header("Authorization") String token,
            @Field("sr_id") String userId
    );

    @FormUrlEncoded
    @POST("get_rtm_token")
    Call<BaseResponse> getRTMToken(
            @Header("Authorization") String token,
            @Field("sr_id") String srId
    );

    @FormUrlEncoded
    @POST("get_new_notification_count")
    Call<BaseResponse> getNewNotificationCount(
            @Header("Authorization") String token,
            @Field("user_id") String userId
    );

    @FormUrlEncoded
    @POST("set_read_state_notification")
    Call<BaseResponse> setReadStateNotification(
            @Header("Authorization") String token,
            @Field("user_id") String userId
    );

    @FormUrlEncoded
    @POST("clear_notification")
    Call<BaseResponse> clearNotification(
            @Header("Authorization") String token,
            @Field("notification_id") String notificationId
    );

    @FormUrlEncoded
    @POST("set_sr_state")
    Call<BaseResponse> submitState(
            @Header("Authorization") String token,
            @Field("sr_id") String srId,
            @Field("state") int state
    );

    @FormUrlEncoded
    @POST("add_new_note")
    Call<BaseResponse> addNewTechNote(
            @Header("Authorization") String token,
            @Field("note_title") String noteTitle,
            @Field("note_text") String noteText
    );

    @FormUrlEncoded
    @POST("update_note")
    Call<BaseResponse> updateTechNote(
            @Header("Authorization") String token,
            @Field("note_id") String noteId,
            @Field("note_title") String noteTitle,
            @Field("note_text") String noteText
    );

    @FormUrlEncoded
    @POST("delete_note")
    Call<BaseResponse> deleteNoteFile(
            @Header("Authorization") String token,
            @Field("note_id") String noteId
    );

    @FormUrlEncoded
    @POST("update_location")
    Call<BaseResponse> updateLocation(
            @Header("Authorization") String token,
            @Field("company_name") String companyName,
            @Field("office_address") String officeAddress,
            @Field("country") String country
    );

    @FormUrlEncoded
    @POST("get_my_technician_list")
    Call<TechnicianInfoRes> getMyTechnicianList(
            @Header("Authorization") String token,
            @Field("page") int page
    );

    @FormUrlEncoded
    @POST("add_my_technician")
    Call<BaseResponse> addNewTechnician(
            @Header("Authorization") String token,
            @Field("technician_id") String vendorId
    );

    @FormUrlEncoded
    @POST("delete_my_technician")
    Call<BaseResponse> deleteTechnician(
            @Header("Authorization") String token,
            @Field("technician_id") String vendorId
    );

    @FormUrlEncoded
    @POST("search_technician")
    Call<TechnicianInfoRes> getAllTechnicianList(
            @Header("Authorization") String token,
            @Field("user_id") String userId,
            @Field("skill_id") String skillIds
    );

    @FormUrlEncoded
    @POST("get_my_sr_list")
    Call<ServiceRequestDataRes> getMySR(
            @Header("Authorization") String token,
            @Field("search") String filter,
            @Field("is_date") int isDate,
            @Field("page") int page
    );

    @FormUrlEncoded
    @POST("get_my_tech_note_list")
    Call<MyTechNoteDataRes> getMyTechNote(
            @Header("Authorization") String token,
            @Field("search") String filter,
            @Field("page") int page
    );

    @FormUrlEncoded
    @POST("get_my_tech_video_list")
    Call<MyTechVideoDataRes> getMyTechVideo(
            @Header("Authorization") String token,
            @Field("search") String filter,
            @Field("page") int page
    );

    @Multipart
    @POST("upload_my_tech_text")
    Call<BaseResponse> uploadMyTechText(
            @Header("Authorization") String token,
            @Part("size") RequestBody fileSize,
            @Part("title") RequestBody rbTitle,
            @Part MultipartBody.Part myTechText
    );

    @Multipart
    @POST("upload_my_tech_video")
    Call<BaseResponse> uploadMyTechVideo(
            @Header("Authorization") String token,
            @Part("size") RequestBody fileSize,
            @Part("title") RequestBody rbTitle,
            @Part MultipartBody.Part myTechVideo
    );

    @FormUrlEncoded
    @POST("get_incoming_sr_list")
    Call<IncomingRequestInfoRes> getIncomingSR(
            @Header("Authorization") String token,
            @Field("search") String filter,
            @Field("page") int page
    );

    @FormUrlEncoded
    @POST("get_notification_list")
    Call<NotificationInfoRes> getNotificationList(
            @Header("Authorization") String token,
            @Field("page") int page
    );

    @FormUrlEncoded
    @POST("get_accepted_sr_list")
    Call<IncomingRequestInfoRes> getAcceptedSRList(
            @Header("Authorization") String token,
            @Field("search") String filter,
            @Field("page") int page
    );

    @FormUrlEncoded
    @POST("get_user_genba_list")
    Call<ServiceRequestDataRes> getUserGenaList(
            @Header("Authorization") String token,
            @Field("search") String filter,
            @Field("page") int page
    );

    @FormUrlEncoded
    @POST("get_vendor_genba_list")
    Call<ServiceRequestDataRes> getVendorGenaList(
            @Header("Authorization") String token,
            @Field("search") String filter,
            @Field("page") int page
    );

    @FormUrlEncoded
    @POST("get_equipment_list")
    Call<EquipmentInfoRes> getMyEquipment(
            @Header("Authorization") String token,
            @Field("search") String filter,
            @Field("page") int page
    );

    @FormUrlEncoded
    @POST("get_equipment_all_list")
    Call<EquipmentInfoRes> getMyEquipmentAll(
            @Header("Authorization") String token,
            @Field("search") String filter
    );

    @FormUrlEncoded
    @POST("get_competence_list")
    Call<CompetenceInfoRes> getMyCompetence(
            @Header("Authorization") String token,
            @Field("filter") String filter,
            @Field("page") int page
    );

    @FormUrlEncoded
    @POST("add_equipment")
    Call<EquipmentInfoRes> addEquipment(
            @Header("Authorization") String token,
            @Field("location") String location,
            @Field("asset_name") String assetName,
            @Field("internal_id") String internalId,
            @Field("make_id") String makeId,
            @Field("model_id") String modelId,
            @Field("warranty_start_date") String wStartDate,
            @Field("warranty_period") String wPeriod,
            @Field("equipment_id") String eqId
    );

    @FormUrlEncoded
    @POST("get_service_type_vendor_names")
    Call<ServiceTypeDataRes> getServiceTypeNVendorNames(
            @Header("Authorization") String token,
            @Field("user_id") String userId
    );

    @FormUrlEncoded
    @POST("send_service_request")
    Call<BaseResponse> sendServiceRequest(
            @Header("Authorization") String token,
            @Field("service_type") String serviceTypeId,
            @Field("equipment_id") String equipmentId,
            @Field("preferred_time") String preferredDateTime,
            @Field("technician_name") String technicianName,
            @Field("description") String description
    );

    @FormUrlEncoded
    @POST("accept_sr")
    Call<BaseResponse> acceptSR(
            @Header("Authorization") String token,
            @Field("sr_id") String serviceTypeId,
            @Field("appointment_date") String appointmentDateTime,
            @Field("duration") String duration
    );

    @FormUrlEncoded
    @POST("decline_sr")
    Call<BaseResponse> declineSR(
            @Header("Authorization") String token,
            @Field("sr_id") String serviceTypeId
    );

    @FormUrlEncoded
    @POST("reschedule_sr")
    Call<BaseResponse> rescheduleSR(
            @Header("Authorization") String token,
            @Field("sr_id") String serviceTypeId,
            @Field("appointment_date") String appointmentDateTime,
            @Field("duration") String preferredDateTime
    );

    @FormUrlEncoded
    @POST("auth/check_account")
    Call<BaseResponse> checkAccount(
            @Field("user_name") String userName,
            @Field("email") String email
    );

    @FormUrlEncoded
    @POST("auth/send_otp")
    Call<BaseResponse> sendOtp(
            @Field("user_name") String userName,
            @Field("email") String email
    );


    @FormUrlEncoded
    @POST("auth/reset_password")
    Call<BaseResponse> resetPassword(
            @Field("otp") String otp,
            @Field("email") String email,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("start_video_record")
    Call<BaseResponse> startRecordVideo(
            @Header("Authorization") String token,
            @Field("sr_id") String srID,
            @Field("uid") String uid
    );

    @FormUrlEncoded
    @POST("stop_video_record")
    Call<BaseResponse> stopRecordVideo(
            @Header("Authorization") String token,
            @Field("record_id") String srID
    );

    @FormUrlEncoded
    @POST("check_video_record")
    Call<BaseResponse> checkRecordVideo(
            @Header("Authorization") String token,
            @Field("record_id") String srID
    );

    @FormUrlEncoded
    @POST("check_sr_state")
    Call<BaseResponse> checkSRState(
            @Header("Authorization") String token,
            @Field("user_id") String userId
    );

}
