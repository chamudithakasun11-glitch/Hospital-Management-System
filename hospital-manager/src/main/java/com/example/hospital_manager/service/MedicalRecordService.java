package com.example.hospital_manager.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.hospital_manager.dto.MedicalRecordRequest;
import com.example.hospital_manager.dto.MedicalRecordResponse;
import com.example.hospital_manager.exception.ApiException;
import com.example.hospital_manager.model.AppUser;
import com.example.hospital_manager.model.MedicalRecord;
import com.example.hospital_manager.model.Patient;
import com.example.hospital_manager.repository.MedicalRecordRepository;
import com.example.hospital_manager.repository.PatientRepository;
import com.example.hospital_manager.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MedicalRecordService {

    private final MedicalRecordRepository medicalRecordRepository;
    private final PatientRepository patientRepository;
    private final UserRepository userRepository;

    // Creates a medical record using the authenticated doctor from the JWT token
    public MedicalRecordResponse createRecord(MedicalRecordRequest request, String doctorUsername) {
        Patient patient = patientRepository.findById(request.patientId())
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Patient not found"));

        AppUser doctor = userRepository.findByUsername(doctorUsername)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Doctor account not found"));

        MedicalRecord record = MedicalRecord.builder()
                .patient(patient)
                .doctor(doctor)
                .diagnosis(request.diagnosis())
                .prescription(request.prescription())
                .notes(request.notes())
                .createdAt(LocalDateTime.now())
                .build();

        return toResponse(medicalRecordRepository.save(record));
    }

    public List<MedicalRecordResponse> getRecordsByPatient(Long patientId) {
        if (!patientRepository.existsById(patientId)) {
            throw new ApiException(HttpStatus.NOT_FOUND, "Patient not found");
        }
        return medicalRecordRepository.findByPatientId(patientId).stream()
                .map(this::toResponse)
                .toList();
    }

    private MedicalRecordResponse toResponse(MedicalRecord r) {
        return new MedicalRecordResponse(
                r.getId(),
                r.getPatient().getId(),
                r.getPatient().getFullName(),
                r.getDoctor().getId(),
                r.getDoctor().getFullName(),
                r.getDiagnosis(),
                r.getPrescription(),
                r.getNotes(),
                r.getCreatedAt()
        );
    }
}