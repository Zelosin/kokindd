package ru.codeinside.test;

import jakarta.persistence.EntityManager;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NoteService {

  private final EntityManager entityManager;

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  List<Note> getAllVersions(Long id) {
    return AuditReaderFactory.get(entityManager)
        .createQuery()
        .forRevisionsOfEntity(Note.class, true, true)
        .add(AuditEntity.id().eq(id))
        .getResultList();
  }
}
