package com.squarenine.squarenine.service;

import java.util.List;
import java.util.Optional;

import com.squarenine.squarenine.entity.FormsInvestEntity;
import com.squarenine.squarenine.entity.FormsSellEntity;

public interface FormsService {
    Optional<FormsInvestEntity> getFormInvest(Long id);

    List<FormsInvestEntity> getFormsInvest();
    
    List<FormsInvestEntity> getFormsInvest(String area);

    FormsInvestEntity addFormInvest(FormsInvestEntity forumsEntity);

    Optional<FormsSellEntity> getFormSell(Long id);

    List<FormsSellEntity> getFormsSell();

    FormsSellEntity addFormSell(FormsSellEntity forumsEntity);

    List<FormsSellEntity> getFormsSell(String area);
}
