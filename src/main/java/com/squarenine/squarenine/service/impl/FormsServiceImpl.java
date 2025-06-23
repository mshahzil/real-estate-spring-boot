package com.squarenine.squarenine.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.squarenine.squarenine.entity.FormsInvestEntity;
import com.squarenine.squarenine.entity.FormsSellEntity;
import com.squarenine.squarenine.repository.FormsInvestRepository;
import com.squarenine.squarenine.repository.FormsSellRepository;
import com.squarenine.squarenine.repository.UserRepository;
import com.squarenine.squarenine.service.FormsService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FormsServiceImpl implements FormsService {

    private final FormsInvestRepository investRepository;
    private final FormsSellRepository sellRepository;
    private final UserRepository userRepository;

    @Override
    public Optional<FormsInvestEntity> getFormInvest(Long id) {
        return investRepository.findById(id);
    }

    @Override
    public List<FormsInvestEntity> getFormsInvest() {
        return investRepository.findAll();
    }

    @Override
    public List<FormsInvestEntity> getFormsInvest(String area) {
        return investRepository.findAllByArea(area);
    }

    @Override
    public FormsInvestEntity addFormInvest(FormsInvestEntity forumsEntity) {
        forumsEntity.setUser_id(userRepository.findByUid(forumsEntity.getUid()).get().getId());
        forumsEntity.setArea(userRepository.findByUid(forumsEntity.getUid()).get().getArea());
        return investRepository.save(forumsEntity);
    }

    @Override
    public Optional<FormsSellEntity> getFormSell(Long id) {
        return sellRepository.findById(id);
    }

    @Override
    public List<FormsSellEntity> getFormsSell() {
        return sellRepository.findAll();
    }

    @Override
    public List<FormsSellEntity> getFormsSell(String area) {
        return sellRepository.findAllByArea(area);
    }

    @Override
    public FormsSellEntity addFormSell(FormsSellEntity forumsEntity) {
        forumsEntity.setUser_id(userRepository.findByUid(forumsEntity.getUid()).get().getId());
        forumsEntity.setArea(userRepository.findByUid(forumsEntity.getUid()).get().getArea());
        return sellRepository.save(forumsEntity);
    }

}
