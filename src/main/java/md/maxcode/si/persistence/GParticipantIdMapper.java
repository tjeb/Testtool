/*
 * Copyright (c) 2015 Maxcode B.V.
 */

package md.maxcode.si.persistence;

import md.maxcode.si.smp.galaxygateway.GParticipantIdentifierReference;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface GParticipantIdMapper {

    void insert(GParticipantIdentifierReference value);

    void remove(Long id);

    @Select("SELECT participantIdValue FROM g_participant_id_ref WHERE userId = #{id}")
    List<String> getParticipantIds(Long id);

    @Select("SELECT * FROM g_participant_id_ref WHERE id = #{id}")
    Map getById(Long id);

    void update(GParticipantIdentifierReference value);

    @Select("SELECT * FROM g_participant_id_ref WHERE participantIdValue = #{participantId}")
    GParticipantIdentifierReference getByParticipantId(String participantId);

    void removeByParticipantId(String participantId);
}
