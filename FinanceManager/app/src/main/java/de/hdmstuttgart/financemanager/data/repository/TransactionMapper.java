package com.hdmstuttgart.financemanager.data.repository;

import com.hdmstuttgart.financemanager.data.local.TransactionEntity;
import com.hdmstuttgart.financemanager.domain.model.TransactionDetail;
import java.util.ArrayList;
import java.util.List;

public class TransactionMapper {

    public static TransactionDetail toDomain(TransactionEntity entity) {
        if (entity == null) return null;
        return new TransactionDetail(
                entity.uid,
                entity.imageResource,
                entity.purpose,
                entity.amount,
                entity.date,
                entity.category,
                entity.method
        );
    }

    public static TransactionEntity toEntity(TransactionDetail domain) {
        if (domain == null) return null;
        TransactionEntity entity = new TransactionEntity(
                domain.getImageResource(),
                domain.getPurpose(),
                domain.getAmount(),
                domain.getDate(),
                domain.getCategory(),
                domain.getMethod()
        );
        entity.uid = domain.getId();
        return entity;
    }

    public static List<TransactionDetail> toDomainList(List<TransactionEntity> entities) {
        List<TransactionDetail> domainList = new ArrayList<>();
        if (entities != null) {
            for (TransactionEntity entity : entities) {
                domainList.add(toDomain(entity));
            }
        }
        return domainList;
    }
}
