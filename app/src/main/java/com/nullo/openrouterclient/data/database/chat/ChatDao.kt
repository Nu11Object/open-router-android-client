package com.nullo.openrouterclient.data.database.chat

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatDao {

    @Query("SELECT * FROM messages")
    fun getMessages(): Flow<List<MessageDbEntity>>

    @Query("SELECT * FROM messages WHERE id = :id LIMIT 1")
    suspend fun getMessageById(id: Long): MessageDbEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(messageDbEntity: MessageDbEntity): Long

    @Update
    suspend fun updateMessage(messageDbEntity: MessageDbEntity)

    @Query(
        """UPDATE messages
        SET 
          isLoading = 0,
          text = :errorText,
          error = :errorHeader
        WHERE isLoading = 1
    """
    )
    suspend fun failLoadingMessages(
        errorHeader: String,
        errorText: String,
    )

    @Query(
        """
        UPDATE messages
        SET 
            isLoading = 0,
            text = :errorText,
            error = :errorHeader
        WHERE id = :id
    """
    )
    suspend fun setErrorIntoMessageById(
        id: Long,
        errorHeader: String,
        errorText: String
    )

    @Query(
        """
        UPDATE messages
        SET 
            text = :responseText,
            reasoning = :responseReasoning,
            isLoading = 0,
            error = NULL
        WHERE id = :messageId
    """
    )
    suspend fun setResponseIntoMessageById(
        messageId: Long,
        responseText: String,
        responseReasoning: String?
    )

    @Delete
    suspend fun deleteMessage(messageDbEntity: MessageDbEntity)

    @Query("DELETE FROM messages WHERE id = :id")
    suspend fun deleteMessageById(id: Long): Int

    @Query("DELETE FROM messages")
    suspend fun clearAllMessages()

}
