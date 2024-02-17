package com.sparta.newsfeed.domain;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class Timestamped {
    /*
    @MappedSuperclass : 하단의 추상클래스에 선언한 멤버 변수를 Column으로 인식해줌
    updatable = false : 이후에 최초 작성시간 수정 안되게끔 업뎃 막음
    TemporalType.TIMESTAMP) : java의 날짜 데이터 매핑 시 사용
    @LastModifiedDate : 시간 변경될 때마다 자동으로 저장
     */
    @CreatedDate
    @Column(updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime modifiedAt;

}
