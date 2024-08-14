package com.example.mytask.repository;

import com.example.mytask.model.Assignee;
import java.util.*;


// JPA안쓰고 JDBC로 할거여서 SQL직접 작성해서 상호작용해야함
// 직접 SQL문을 작성해서 DB와 상호작용하려면 따로 인터페이스 구현해서 사용?
public interface AssigneeRepository {

    // 담당자 생성
    void save(Assignee assignee);

    // 특정 ID 담당자 조회
    Optional<Assignee> findById(int id);

    // 모든 담당자 조회
    List<Assignee> findAll();

    // 담당자 수정
    void update(Assignee assignee);

    // 담당자 삭제
    void deleteById(int id);
}
