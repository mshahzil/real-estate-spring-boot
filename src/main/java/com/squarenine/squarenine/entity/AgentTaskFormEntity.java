package com.squarenine.squarenine.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "agent_task_form")
public class AgentTaskFormEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "agent_name")
    private String agentName;
    private String area;
    private String date;
    @Column(name = "new_calls")
    private int newCalls;
    @Column(name = "follow_ups")
    private int followUps;
    @Column(name = "total_calls")
    private int totalCalls;
    private int meetings;
    private int visits;
    @Column(name = "expected_payments")
    private int expectedPayments;
    private String manager;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agent_id", nullable = false)
    private AgentEntity agentEntity;
    
    @OneToMany(mappedBy = "agentTaskFormEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ClientReportEntity> clientReports;
}