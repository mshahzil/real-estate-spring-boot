package com.squarenine.squarenine.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "client_report")
public class ClientReportEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "client_name")
    private String clientName;
    private String remarks;
    @Column(name = "expected_payment")
    private int expectedPayment;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_form_id")
    @JsonIgnore
    private AgentTaskFormEntity agentTaskFormEntity;
}
