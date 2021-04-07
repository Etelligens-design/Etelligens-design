package com.pch.ete.retrofit;

import android.text.TextUtils;
import android.util.Log;

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
import com.pch.ete.ui.technical_note.model.MyTechNoteDataRes;
import com.pch.ete.ui.technical_video.model.MyTechVideoDataRes;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiCall {
    private static APIService service;

    public static ApiCall getInstance() {
        if (service == null) {
            service = RestClient.getClient();
        }
        return new ApiCall();
    }

    public void register(int userType, String userName, String fullName, String email, String phone, String password,
                         final IApiCallback<LoginDataRes> iApiCallback) {

        Call<LoginDataRes> call = service.register(userType, userName, fullName, email, phone, password);
        call.enqueue(new Callback<LoginDataRes>() {
            @Override
            public void onResponse(Call<LoginDataRes> call, Response<LoginDataRes> response) {
                iApiCallback.onSuccess("register", response);
            }

            @Override
            public void onFailure(Call<LoginDataRes> call, Throwable t) {
                iApiCallback.onFailure("" + t.getMessage());
            }
        });
    }

    public void login(String email, String password,
                      final IApiCallback<LoginDataRes> iApiCallback) {

        Call<LoginDataRes> call = service.login(email, password);
        call.enqueue(new Callback<LoginDataRes>() {
            @Override
            public void onResponse(Call<LoginDataRes> call, Response<LoginDataRes> response) {
                iApiCallback.onSuccess("login", response);
            }

            @Override
            public void onFailure(Call<LoginDataRes> call, Throwable t) {
                iApiCallback.onFailure("" + t.getMessage());
            }
        });
    }

    public void loginWithToken(String token,
                               final IApiCallback<LoginDataRes> iApiCallback) {

        Call<LoginDataRes> call = service.loginWithToken("Bearer " + token, "login with token");
        call.enqueue(new Callback<LoginDataRes>() {
            @Override
            public void onResponse(Call<LoginDataRes> call, Response<LoginDataRes> response) {
                iApiCallback.onSuccess("login", response);
            }

            @Override
            public void onFailure(Call<LoginDataRes> call, Throwable t) {
                iApiCallback.onFailure("" + t.getMessage());
            }
        });
    }

    public void updateDeviceToken(String token, String deviceToken,
                                  final IApiCallback<BaseResponse> iApiCallback) {

        Call<BaseResponse> call = service.updateDeviceToken("Bearer " + token, deviceToken);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                iApiCallback.onSuccess("update_device_token", response);
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                iApiCallback.onFailure("" + t.getMessage());
            }
        });
    }

    public void updateProfileImage(String token, String image,
                                   final IApiCallback<BaseResponse> iApiCallback) {

        if (image == null)
            image = "";

        MultipartBody.Part profileLogo = null;
        if (!TextUtils.isEmpty(image)) {
            File file = new File(image);
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            profileLogo = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
        }

        Call<BaseResponse> call = service.updateProfileImage("Bearer " + token, profileLogo);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                iApiCallback.onSuccess("update_profile_image", response);
               // Log.d("dssdfkd: ", "" + response);
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                iApiCallback.onFailure("" + t.getMessage());
            }
        });
    }

    public void updateUserName(String token, String userName,
                               final IApiCallback<BaseResponse> iApiCallback) {

        Call<BaseResponse> call = service.updateUserName("Bearer " + token, userName);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                iApiCallback.onSuccess("update_user_name", response);
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                iApiCallback.onFailure("" + t.getMessage());
            }
        });
    }

    public void updateUserRole(String token, String roleName,
                               final IApiCallback<BaseResponse> iApiCallback) {

        Call<BaseResponse> call = service.updateUserRole("Bearer " + token, roleName);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                iApiCallback.onSuccess("update_role", response);
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                iApiCallback.onFailure("" + t.getMessage());
            }
        });
    }

    public void updateUserDescription(String token, String description,
                                      final IApiCallback<BaseResponse> iApiCallback) {

        Call<BaseResponse> call = service.updateUserDescription("Bearer " + token, description);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                iApiCallback.onSuccess("update_description", response);
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                iApiCallback.onFailure("" + t.getMessage());
            }
        });
    }

    public void updateFullName(String token, String fullName,
                               final IApiCallback<BaseResponse> iApiCallback) {

        Call<BaseResponse> call = service.updateFullName("Bearer " + token, fullName);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                iApiCallback.onSuccess("update_full_name", response);
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                iApiCallback.onFailure("" + t.getMessage());
            }
        });
    }

//    public void updateEmail(String token, String email,
//                            final IApiCallback<BaseResponse> iApiCallback) {
//
//        Call<BaseResponse> call = service.updateEmail("Bearer " + token, email);
//        call.enqueue(new Callback<BaseResponse>() {
//            @Override
//            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
//                iApiCallback.onSuccess("update_email", response);
//            }
//
//            @Override
//            public void onFailure(Call<BaseResponse> call, Throwable t) {
//                iApiCallback.onFailure("" + t.getMessage());
//            }
//        });
//    }

    public void updatePhone(String token, String phone,
                            final IApiCallback<BaseResponse> iApiCallback) {

        Call<BaseResponse> call = service.updatePhone("Bearer " + token, phone);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                iApiCallback.onSuccess("update_phone", response);
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                iApiCallback.onFailure("" + t.getMessage());
            }
        });
    }

    public void getMakeModelSkillList(String token, String userId,
                                      final IApiCallback<MakeModelSkillRes> iApiCallback) {

        Call<MakeModelSkillRes> call = service.getMakeModelSkillList("Bearer " + token, userId);
        call.enqueue(new Callback<MakeModelSkillRes>() {
            @Override
            public void onResponse(Call<MakeModelSkillRes> call, Response<MakeModelSkillRes> response) {
                iApiCallback.onSuccess("make_model", response);
            }

            @Override
            public void onFailure(Call<MakeModelSkillRes> call, Throwable t) {
                iApiCallback.onFailure("" + t.getMessage());
            }
        });
    }


    public void addSkill(String token, String makeId, String modelId, String skillId, String certificatedDocPath, String authDocPath,
                         final IApiCallback<UserSkillDataRes> iApiCallback) {

        if (certificatedDocPath == null)
            certificatedDocPath = "";
        if (authDocPath == null)
            authDocPath = "";
        MultipartBody.Part certificatedDoc = null, authDoc = null;
        if (!TextUtils.isEmpty(certificatedDocPath)) {
            File file = new File(certificatedDocPath);
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            certificatedDoc = MultipartBody.Part.createFormData("certificated_doc", file.getName(), requestFile);
        }

        if (!TextUtils.isEmpty(authDocPath)) {
            File file = new File(authDocPath);
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            authDoc = MultipartBody.Part.createFormData("auth_doc", file.getName(), requestFile);
        }

        RequestBody rbMakeId = RequestBody.create(MediaType.parse("multipart/form-data"), makeId);
        RequestBody rbModelId = RequestBody.create(MediaType.parse("multipart/form-data"), modelId);
        RequestBody rbSkillId = RequestBody.create(MediaType.parse("multipart/form-data"), skillId);

        Call<UserSkillDataRes> call = service.addSkill("Bearer " + token, rbMakeId, rbModelId, rbSkillId, certificatedDoc, authDoc);
        call.enqueue(new Callback<UserSkillDataRes>() {
            @Override
            public void onResponse(Call<UserSkillDataRes> call, Response<UserSkillDataRes> response) {
                iApiCallback.onSuccess("add_skill", response);
            }

            @Override
            public void onFailure(Call<UserSkillDataRes> call, Throwable t) {
                iApiCallback.onFailure("" + t.getMessage());
            }
        });
    }

    public void addCompetence(String token, String makeId, String modelId, String skillId, String certificatedDocPath, String authDocPath, String serviceHour,
                              final IApiCallback<CompetenceInfoRes> iApiCallback) {

        if (certificatedDocPath == null)
            certificatedDocPath = "";
        if (authDocPath == null)
            authDocPath = "";
        MultipartBody.Part certificatedDoc = null, authDoc = null;
        if (!TextUtils.isEmpty(certificatedDocPath)) {
            File file = new File(certificatedDocPath);
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            certificatedDoc = MultipartBody.Part.createFormData("certificated_doc", file.getName(), requestFile);
        }

        if (!TextUtils.isEmpty(authDocPath)) {
            File file = new File(authDocPath);
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            authDoc = MultipartBody.Part.createFormData("auth_doc", file.getName(), requestFile);
        }

        RequestBody rbMakeId = RequestBody.create(MediaType.parse("multipart/form-data"), makeId);
        RequestBody rbModelId = RequestBody.create(MediaType.parse("multipart/form-data"), modelId);
        RequestBody rbSkillId = RequestBody.create(MediaType.parse("multipart/form-data"), skillId);
        RequestBody rbServiceHour = RequestBody.create(MediaType.parse("multipart/form-data"), serviceHour);

        Call<CompetenceInfoRes> call = service.addCompetence("Bearer " + token, rbMakeId, rbModelId, rbSkillId, rbServiceHour, certificatedDoc, authDoc);
        call.enqueue(new Callback<CompetenceInfoRes>() {
            @Override
            public void onResponse(Call<CompetenceInfoRes> call, Response<CompetenceInfoRes> response) {
                iApiCallback.onSuccess("add_competence", response);
            }

            @Override
            public void onFailure(Call<CompetenceInfoRes> call, Throwable t) {
                iApiCallback.onFailure("" + t.getMessage());
            }
        });
    }

    public void updateCompetence(String token, String competenceId, String makeId, String modelId, String skillId, String certificatedDocPath, String authDocPath, String serviceHour,
                                 final IApiCallback<CompetenceInfoRes> iApiCallback) {

        if (certificatedDocPath == null)
            certificatedDocPath = "";
        if (authDocPath == null)
            authDocPath = "";
        MultipartBody.Part certificatedDoc = null, authDoc = null;
        if (!TextUtils.isEmpty(certificatedDocPath)) {
            File file = new File(certificatedDocPath);
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            certificatedDoc = MultipartBody.Part.createFormData("certificated_doc", file.getName(), requestFile);
        }

        if (!TextUtils.isEmpty(authDocPath)) {
            File file = new File(authDocPath);
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            authDoc = MultipartBody.Part.createFormData("auth_doc", file.getName(), requestFile);
        }

        RequestBody rbCompetenceId = RequestBody.create(MediaType.parse("multipart/form-data"), competenceId);
        RequestBody rbMakeId = RequestBody.create(MediaType.parse("multipart/form-data"), makeId);
        RequestBody rbModelId = RequestBody.create(MediaType.parse("multipart/form-data"), modelId);
        RequestBody rbSkillId = RequestBody.create(MediaType.parse("multipart/form-data"), skillId);
        RequestBody rbServiceHour = RequestBody.create(MediaType.parse("multipart/form-data"), serviceHour);

        Call<CompetenceInfoRes> call = service.updateCompetence("Bearer " + token, rbCompetenceId, rbMakeId, rbModelId, rbSkillId, rbServiceHour, certificatedDoc, authDoc);
        call.enqueue(new Callback<CompetenceInfoRes>() {
            @Override
            public void onResponse(Call<CompetenceInfoRes> call, Response<CompetenceInfoRes> response) {
                iApiCallback.onSuccess("add_competence", response);
            }

            @Override
            public void onFailure(Call<CompetenceInfoRes> call, Throwable t) {
                iApiCallback.onFailure("" + t.getMessage());
            }
        });
    }

    public void getUserSkills(String token, String userId,
                              final IApiCallback<UserSkillDataRes> iApiCallback) {

        Call<UserSkillDataRes> call = service.getUserSkills("Bearer " + token, userId);
        call.enqueue(new Callback<UserSkillDataRes>() {
            @Override
            public void onResponse(Call<UserSkillDataRes> call, Response<UserSkillDataRes> response) {
                iApiCallback.onSuccess("user_skills", response);
            }

            @Override
            public void onFailure(Call<UserSkillDataRes> call, Throwable t) {
                iApiCallback.onFailure("" + t.getMessage());
            }
        });
    }

    public void getOtherUserSkills(String token, String userId,
                                   final IApiCallback<UserSkillDataRes> iApiCallback) {

        Call<UserSkillDataRes> call = service.getOtherUserSkills("Bearer " + token, userId);
        call.enqueue(new Callback<UserSkillDataRes>() {
            @Override
            public void onResponse(Call<UserSkillDataRes> call, Response<UserSkillDataRes> response) {
                iApiCallback.onSuccess("user_skills", response);
            }

            @Override
            public void onFailure(Call<UserSkillDataRes> call, Throwable t) {
                iApiCallback.onFailure("" + t.getMessage());
            }
        });
    }

    public void updateLocation(String token, String companyName, String companyAddress, String country,
                               final IApiCallback<BaseResponse> iApiCallback) {

        Call<BaseResponse> call = service.updateLocation("Bearer " + token, companyName, companyAddress, country);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                iApiCallback.onSuccess("update_location", response);
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                iApiCallback.onFailure("" + t.getMessage());
            }
        });
    }

    public void getMyTechnicianList(String token, int page,
                                    final IApiCallback<TechnicianInfoRes> iApiCallback) {

        Call<TechnicianInfoRes> call = service.getMyTechnicianList("Bearer " + token, page);
        call.enqueue(new Callback<TechnicianInfoRes>() {
            @Override
            public void onResponse(Call<TechnicianInfoRes> call, Response<TechnicianInfoRes> response) {
                iApiCallback.onSuccess("my_technician_list", response);
            }

            @Override
            public void onFailure(Call<TechnicianInfoRes> call, Throwable t) {
                iApiCallback.onFailure("" + t.getMessage());
            }
        });
    }

    public void addNewTechnician(String token, String newVendorId,
                                 final IApiCallback<BaseResponse> iApiCallback) {

        Call<BaseResponse> call = service.addNewTechnician("Bearer " + token, newVendorId);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                iApiCallback.onSuccess("add_technician", response);
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                iApiCallback.onFailure("" + t.getMessage());
            }
        });
    }

    public void deleteTechnician(String token, String newVendorId,
                                 final IApiCallback<BaseResponse> iApiCallback) {

        Call<BaseResponse> call = service.deleteTechnician("Bearer " + token, newVendorId);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                iApiCallback.onSuccess("delete_technician", response);
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                iApiCallback.onFailure("" + t.getMessage());
            }
        });
    }

    public void getAllTechnicianList(String token, String userId, String skillIds,
                                     final IApiCallback<TechnicianInfoRes> iApiCallback) {

        Call<TechnicianInfoRes> call = service.getAllTechnicianList("Bearer " + token, userId, skillIds);
        call.enqueue(new Callback<TechnicianInfoRes>() {
            @Override
            public void onResponse(Call<TechnicianInfoRes> call, Response<TechnicianInfoRes> response) {
                iApiCallback.onSuccess("all_technician_list", response);
            }

            @Override
            public void onFailure(Call<TechnicianInfoRes> call, Throwable t) {
                iApiCallback.onFailure("" + t.getMessage());
            }
        });
    }

    public void getMySR(String token, String search, int isDate, int page,
                        final IApiCallback<ServiceRequestDataRes> iApiCallback) {

        if (search.toUpperCase().startsWith("SR#")) {
            search = search.toUpperCase().replace("SR#", "");
        }
        Call<ServiceRequestDataRes> call = service.getMySR("Bearer " + token, search, isDate, page);
        call.enqueue(new Callback<ServiceRequestDataRes>() {
            @Override
            public void onResponse(Call<ServiceRequestDataRes> call, Response<ServiceRequestDataRes> response) {
                iApiCallback.onSuccess("my_sr_list", response);
            }

            @Override
            public void onFailure(Call<ServiceRequestDataRes> call, Throwable t) {
                iApiCallback.onFailure("" + t.getMessage());
            }
        });
    }

    public void getMyTechNote(String token, String search, int page,
                              final IApiCallback<MyTechNoteDataRes> iApiCallback) {

        Call<MyTechNoteDataRes> call = service.getMyTechNote("Bearer " + token, search, page);
        call.enqueue(new Callback<MyTechNoteDataRes>() {
            @Override
            public void onResponse(Call<MyTechNoteDataRes> call, Response<MyTechNoteDataRes> response) {
                iApiCallback.onSuccess("my_tech_note_list", response);
            }

            @Override
            public void onFailure(Call<MyTechNoteDataRes> call, Throwable t) {
                iApiCallback.onFailure("" + t.getMessage());
            }
        });
    }

    public void uploadMyTechText(String token, File textFile, String title,
                                 final IApiCallback<BaseResponse> iApiCallback) {

        MultipartBody.Part myTechVideoFile = null;
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), textFile);
        myTechVideoFile = MultipartBody.Part.createFormData("text", textFile.getName(), requestFile);

        String strSize = textFile.length() / 1024 + "." + textFile.length() % 1024;
        strSize = String.format("%.2fKB", Float.parseFloat(strSize));
        RequestBody rbFileSize = RequestBody.create(MediaType.parse("multipart/form-data"), strSize);
        RequestBody rbTitle = RequestBody.create(MediaType.parse("multipart/form-data"), title);

        Call<BaseResponse> call = service.uploadMyTechText("Bearer " + token, rbFileSize, rbTitle, myTechVideoFile);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                iApiCallback.onSuccess("upload_text", response);
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                iApiCallback.onFailure("" + t.getMessage());
            }
        });
    }

    public void uploadMyTechVideo(String token, File videoFile, String title,
                                  final IApiCallback<BaseResponse> iApiCallback) {

        MultipartBody.Part myTechVideoFile = null;
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), videoFile);
        myTechVideoFile = MultipartBody.Part.createFormData("video", videoFile.getName(), requestFile);

        String strSize = videoFile.length() / (1024 * 1024) + "." + videoFile.length() % (1024 * 1024);
        strSize = String.format("%.2fMB", Float.parseFloat(strSize));
        RequestBody rbFileSize = RequestBody.create(MediaType.parse("multipart/form-data"), strSize);
        RequestBody rbTitle = RequestBody.create(MediaType.parse("multipart/form-data"), title);

        Call<BaseResponse> call = service.uploadMyTechVideo("Bearer " + token, rbFileSize, rbTitle, myTechVideoFile);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                iApiCallback.onSuccess("upload_video", response);
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                iApiCallback.onFailure("" + t.getMessage());
            }
        });
    }

    public void getMyTechVideo(String token, String search, int page,
                               final IApiCallback<MyTechVideoDataRes> iApiCallback) {

        Call<MyTechVideoDataRes> call = service.getMyTechVideo("Bearer " + token, search, page);
        call.enqueue(new Callback<MyTechVideoDataRes>() {
            @Override
            public void onResponse(Call<MyTechVideoDataRes> call, Response<MyTechVideoDataRes> response) {
                iApiCallback.onSuccess("my_tech_video_list", response);
            }

            @Override
            public void onFailure(Call<MyTechVideoDataRes> call, Throwable t) {
                iApiCallback.onFailure("" + t.getMessage());
            }
        });
    }

    public void getIncomingSR(String token, String search, int page,
                              final IApiCallback<IncomingRequestInfoRes> iApiCallback) {

        Call<IncomingRequestInfoRes> call = service.getIncomingSR("Bearer " + token, search, page);
        call.enqueue(new Callback<IncomingRequestInfoRes>() {
            @Override
            public void onResponse(Call<IncomingRequestInfoRes> call, Response<IncomingRequestInfoRes> response) {
                iApiCallback.onSuccess("incoming_sr_list", response);
            }

            @Override
            public void onFailure(Call<IncomingRequestInfoRes> call, Throwable t) {
                iApiCallback.onFailure("" + t.getMessage());
            }
        });
    }

    public void getNotificationList(String token, int page,
                                    final IApiCallback<NotificationInfoRes> iApiCallback) {

        Call<NotificationInfoRes> call = service.getNotificationList("Bearer " + token, page);
        call.enqueue(new Callback<NotificationInfoRes>() {
            @Override
            public void onResponse(Call<NotificationInfoRes> call, Response<NotificationInfoRes> response) {
                iApiCallback.onSuccess("notification_list", response);
            }

            @Override
            public void onFailure(Call<NotificationInfoRes> call, Throwable t) {
                iApiCallback.onFailure("" + t.getMessage());
            }
        });
    }


    public void getAcceptedSRList(String token, String search, int page,
                                  final IApiCallback<IncomingRequestInfoRes> iApiCallback) {

        Call<IncomingRequestInfoRes> call = service.getAcceptedSRList("Bearer " + token, search, page);
        call.enqueue(new Callback<IncomingRequestInfoRes>() {
            @Override
            public void onResponse(Call<IncomingRequestInfoRes> call, Response<IncomingRequestInfoRes> response) {
                iApiCallback.onSuccess("accepted_sr_list", response);
            }

            @Override
            public void onFailure(Call<IncomingRequestInfoRes> call, Throwable t) {
                iApiCallback.onFailure("" + t.getMessage());
            }
        });
    }


    public void getUserGenaList(String token, String search, int page,
                                final IApiCallback<ServiceRequestDataRes> iApiCallback) {

        Call<ServiceRequestDataRes> call = service.getUserGenaList("Bearer " + token, search, page);
        call.enqueue(new Callback<ServiceRequestDataRes>() {
            @Override
            public void onResponse(Call<ServiceRequestDataRes> call, Response<ServiceRequestDataRes> response) {
                iApiCallback.onSuccess("gena_list", response);
            }

            @Override
            public void onFailure(Call<ServiceRequestDataRes> call, Throwable t) {
                iApiCallback.onFailure("" + t.getMessage());
            }
        });
    }

    public void getVendorGenaList(String token, String search, int page,
                                  final IApiCallback<ServiceRequestDataRes> iApiCallback) {

        Call<ServiceRequestDataRes> call = service.getVendorGenaList("Bearer " + token, search, page);
        call.enqueue(new Callback<ServiceRequestDataRes>() {
            @Override
            public void onResponse(Call<ServiceRequestDataRes> call, Response<ServiceRequestDataRes> response) {
                iApiCallback.onSuccess("gena_list", response);
            }

            @Override
            public void onFailure(Call<ServiceRequestDataRes> call, Throwable t) {
                iApiCallback.onFailure("" + t.getMessage());
            }
        });
    }

    public void getMyEquipmentAll(String token, String filter,
                                  final IApiCallback<EquipmentInfoRes> iApiCallback) {

        Call<EquipmentInfoRes> call = service.getMyEquipmentAll("Bearer " + token, filter);
        call.enqueue(new Callback<EquipmentInfoRes>() {
            @Override
            public void onResponse(Call<EquipmentInfoRes> call, Response<EquipmentInfoRes> response) {
                iApiCallback.onSuccess("equipment_list", response);
            }

            @Override
            public void onFailure(Call<EquipmentInfoRes> call, Throwable t) {
                iApiCallback.onFailure("" + t.getMessage());
            }
        });
    }

    public void getMyEquipment(String token, String filter, int page,
                               final IApiCallback<EquipmentInfoRes> iApiCallback) {

        Call<EquipmentInfoRes> call = service.getMyEquipment("Bearer " + token, filter, page);
        call.enqueue(new Callback<EquipmentInfoRes>() {
            @Override
            public void onResponse(Call<EquipmentInfoRes> call, Response<EquipmentInfoRes> response) {
                iApiCallback.onSuccess("equipment_list", response);
            }

            @Override
            public void onFailure(Call<EquipmentInfoRes> call, Throwable t) {
                iApiCallback.onFailure("" + t.getMessage());
            }
        });
    }

    public void getMyCompetence(String token, String filter, int page,
                                final IApiCallback<CompetenceInfoRes> iApiCallback) {

        Call<CompetenceInfoRes> call = service.getMyCompetence("Bearer " + token, filter, page);
        call.enqueue(new Callback<CompetenceInfoRes>() {
            @Override
            public void onResponse(Call<CompetenceInfoRes> call, Response<CompetenceInfoRes> response) {
                iApiCallback.onSuccess("competence_list", response);
            }

            @Override
            public void onFailure(Call<CompetenceInfoRes> call, Throwable t) {
                iApiCallback.onFailure("" + t.getMessage());
            }
        });
    }


    public void addEquipment(String token, String location, String assetName, String internalId, String makeId, String modelId,
                             String warrantyStartDate, String warrantyPeriod, String eqId, final IApiCallback<EquipmentInfoRes> iApiCallback) {

        Call<EquipmentInfoRes> call = service.addEquipment("Bearer " + token, location, assetName, internalId, makeId, modelId, warrantyStartDate, warrantyPeriod, eqId);
        call.enqueue(new Callback<EquipmentInfoRes>() {
            @Override
            public void onResponse(Call<EquipmentInfoRes> call, Response<EquipmentInfoRes> response) {
                iApiCallback.onSuccess("add_equipment", response);
            }

            @Override
            public void onFailure(Call<EquipmentInfoRes> call, Throwable t) {
                iApiCallback.onFailure("" + t.getMessage());
            }
        });
    }

    public void getServiceTypeNVendorNames(String token, String id,
                                           final IApiCallback<ServiceTypeDataRes> iApiCallback) {

        Call<ServiceTypeDataRes> call = service.getServiceTypeNVendorNames("Bearer " + token, id);
        call.enqueue(new Callback<ServiceTypeDataRes>() {
            @Override
            public void onResponse(Call<ServiceTypeDataRes> call, Response<ServiceTypeDataRes> response) {
                iApiCallback.onSuccess("service_type_vendor_names", response);
            }

            @Override
            public void onFailure(Call<ServiceTypeDataRes> call, Throwable t) {
                iApiCallback.onFailure("" + t.getMessage());
            }
        });
    }

    public void sendServiceRequest(String token, String serviceTypeId,
                                   String equipmentId, String preferredDateTime, String technicianName,
                                   String description, final IApiCallback<BaseResponse> iApiCallback) {

        Call<BaseResponse> call = service.sendServiceRequest("Bearer " + token, serviceTypeId, equipmentId, preferredDateTime, technicianName, description);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                iApiCallback.onSuccess("send_request", response);
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                iApiCallback.onFailure("" + t.getMessage());
            }
        });
    }

    public void acceptSr(String token, String srId, String appointmentDateTime, String duration, final IApiCallback<BaseResponse> iApiCallback) {

        Call<BaseResponse> call = service.acceptSR("Bearer " + token, srId, appointmentDateTime, duration);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                iApiCallback.onSuccess("accept_sr", response);
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                iApiCallback.onFailure("" + t.getMessage());
            }
        });
    }

    public void declineSR(String token, String srId, final IApiCallback<BaseResponse> iApiCallback) {

        Call<BaseResponse> call = service.declineSR("Bearer " + token, srId);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                iApiCallback.onSuccess("decline_sr", response);
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                iApiCallback.onFailure("" + t.getMessage());
            }
        });
    }

    public void rescheduleSR(String token, String srId, String appointmentDateTime, String duration, final IApiCallback<BaseResponse> iApiCallback) {

        Call<BaseResponse> call = service.rescheduleSR("Bearer " + token, srId, appointmentDateTime, duration);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                iApiCallback.onSuccess("reschedule_sr", response);
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                iApiCallback.onFailure("" + t.getMessage());
            }
        });
    }

    public void getSkillList(String token, String userId,
                             final IApiCallback<SkillDataRes> iApiCallback) {

        Call<SkillDataRes> call = service.getSkillList("Bearer " + token, userId);
        call.enqueue(new Callback<SkillDataRes>() {
            @Override
            public void onResponse(Call<SkillDataRes> call, Response<SkillDataRes> response) {
                iApiCallback.onSuccess("skill_list", response);
            }

            @Override
            public void onFailure(Call<SkillDataRes> call, Throwable t) {
                iApiCallback.onFailure("" + t.getMessage());
            }
        });
    }

    public void checkAccount(String userName, String email,
                             final IApiCallback<BaseResponse> iApiCallback) {

        Call<BaseResponse> call = service.checkAccount(userName, email);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                iApiCallback.onSuccess("check_account", response);
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                iApiCallback.onFailure("" + t.getMessage());
            }
        });
    }

    public void sendOtp(String userName, String email,
                        final IApiCallback<BaseResponse> iApiCallback) {

        Call<BaseResponse> call = service.sendOtp(userName, email);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                iApiCallback.onSuccess("send_otp", response);
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                iApiCallback.onFailure("" + t.getMessage());
                Log.d("sjdfkjs: ", "" + t.getMessage());
            }
        });
    }

    public void resetPassword(String otp, String email, String newPassword,
                              final IApiCallback<BaseResponse> iApiCallback) {

        Call<BaseResponse> call = service.resetPassword(otp, email, newPassword);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                iApiCallback.onSuccess("new_password", response);
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                iApiCallback.onFailure("" + t.getMessage());
            }
        });
    }

    public void getToken(String token, String srId,
                         final IApiCallback<BaseResponse> iApiCallback) {

        Call<BaseResponse> call = service.getToken("Bearer " + token, srId);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                iApiCallback.onSuccess("token", response);
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                iApiCallback.onFailure("" + t.getMessage());
            }
        });
    }

    public void getRTMToken(String token, String srId,
                            final IApiCallback<BaseResponse> iApiCallback) {

        Call<BaseResponse> call = service.getRTMToken("Bearer " + token, srId);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                iApiCallback.onSuccess("rtm_token", response);
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                iApiCallback.onFailure("" + t.getMessage());
            }
        });
    }

    public void getNewNotificationCount(String token, String userId,
                                        final IApiCallback<BaseResponse> iApiCallback) {

        Call<BaseResponse> call = service.getNewNotificationCount("Bearer " + token, userId);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                iApiCallback.onSuccess("new_notification", response);
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                iApiCallback.onFailure("" + t.getMessage());
            }
        });
    }

    public void clearNotification(String token, String notiId,
                                  final IApiCallback<BaseResponse> iApiCallback) {

        Call<BaseResponse> call = service.clearNotification("Bearer " + token, notiId);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                iApiCallback.onSuccess("clear_notification", response);
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                iApiCallback.onFailure("" + t.getMessage());
            }
        });
    }

    public void setReadStateNotification(String token, String userId,
                                         final IApiCallback<BaseResponse> iApiCallback) {

        Call<BaseResponse> call = service.setReadStateNotification("Bearer " + token, userId);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
//                iApiCallback.onSuccess("done", response);
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
//                iApiCallback.onFailure("" + t.getMessage());
            }
        });
    }

    public void submitState(String token, String srId, int state,
                            final IApiCallback<BaseResponse> iApiCallback) {

        Call<BaseResponse> call = service.submitState("Bearer " + token, srId, state);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                iApiCallback.onSuccess("set_state", response);
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                iApiCallback.onFailure("" + t.getMessage());
            }
        });
    }

    public void addNewTechNote(String token, String noteTitle, String noteText,
                               final IApiCallback<BaseResponse> iApiCallback) {

        Call<BaseResponse> call = service.addNewTechNote("Bearer " + token, noteTitle, noteText);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                iApiCallback.onSuccess("added_new_note", response);
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                iApiCallback.onFailure("" + t.getMessage());
            }
        });
    }

    public void updateTechNote(String token, String noteId, String noteTitle, String noteText,
                               final IApiCallback<BaseResponse> iApiCallback) {

        Call<BaseResponse> call = service.updateTechNote("Bearer " + token, noteId, noteTitle, noteText);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                iApiCallback.onSuccess("update_note", response);
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                iApiCallback.onFailure("" + t.getMessage());
            }
        });
    }

    public void deleteNoteFile(String token, String noteId,
                               final IApiCallback<BaseResponse> iApiCallback) {

        Call<BaseResponse> call = service.deleteNoteFile("Bearer " + token, noteId);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                iApiCallback.onSuccess("delete_note", response);
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                iApiCallback.onFailure("" + t.getMessage());
            }
        });
    }


    public void startRecordVideo(String token, String srId, String uid,
                                 final IApiCallback<BaseResponse> iApiCallback) {

        Call<BaseResponse> call = service.startRecordVideo("Bearer " + token, srId, uid);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                iApiCallback.onSuccess("start_record", response);
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                iApiCallback.onFailure("" + t.getMessage());
            }
        });
    }

    public void stopRecordVideo(String token, String recordId,
                                final IApiCallback<BaseResponse> iApiCallback) {

        Call<BaseResponse> call = service.stopRecordVideo("Bearer " + token, recordId);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                iApiCallback.onSuccess("stop_record", response);
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                iApiCallback.onFailure("" + t.getMessage());
            }
        });
    }

    public void checkRecordVideo(String token, String recordId,
                                 final IApiCallback<BaseResponse> iApiCallback) {

        Call<BaseResponse> call = service.checkRecordVideo("Bearer " + token, recordId);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                iApiCallback.onSuccess("check_record", response);
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                iApiCallback.onFailure("" + t.getMessage());
            }
        });
    }

    public void checkSRState(String token, String userId,
                             final IApiCallback<BaseResponse> iApiCallback) {

        Call<BaseResponse> call = service.checkSRState("Bearer " + token, userId);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                iApiCallback.onSuccess("check_sr_before_10min", response);
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                iApiCallback.onFailure("" + t.getMessage());
            }
        });
    }


}
