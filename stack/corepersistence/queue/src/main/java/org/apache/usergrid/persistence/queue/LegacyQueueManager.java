/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  The ASF licenses this file to You
 * under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.  For additional information regarding
 * copyright in this work, please see the NOTICE file in the top level
 * directory of this distribution.
 */
package org.apache.usergrid.persistence.queue;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**ctor
 * Manages queues for usergrid.  Current implementation is sqs based.
 */
public interface LegacyQueueManager {

    /**
     * Different implementations
     */
    enum Implementation {
        LOCAL,           // local in-memory queue
        DISTRIBUTED,     // built-in Akka-based queue
        DISTRIBUTED_SNS; // SNS queue
    }

    /**
     * Read messages from queue
     * @param limit
     * @param klass class to cast the return from
     * @return List of Queue Messages
     */
    List<LegacyQueueMessage> getMessages(int limit, Class klass);

    /**
     * get the queue depth
     */
    long getQueueDepth();

    /**
     * Commit the transaction
     * @param queueMessage
     */
    void commitMessage( LegacyQueueMessage queueMessage);

    /**
     * commit multiple messages
     * @param queueMessages
     */
    void commitMessages( List<LegacyQueueMessage> queueMessages);

    /**
     * send messages to queue
     * @param bodies body objects must be serializable
     * @throws IOException
     */
    void sendMessagesAsync(List bodies) throws IOException;

    /**
     * send messages to queue
     * @param bodies body objects must be serializable
     * @throws IOException
     */
    void sendMessages(List bodies) throws IOException;

    /**
     * send messages to queue
     * @param queueMessages
     * @throws IOException
     * @return set of receipt handles for successfully sent messages
     */
    List<LegacyQueueMessage> sendQueueMessages(List<LegacyQueueMessage> queueMessages) throws IOException;

    /**
     * send a message to queue
     * @param body
     * @throws IOException
     */
    <T extends Serializable> void sendMessageToLocalRegion(T body, Boolean async)throws IOException;

    /**
     * Send a messae to the topic to be sent to other queues
     * @param body
     */
    <T extends Serializable> void sendMessageToAllRegions(T body, Boolean async) throws IOException;

    /**
     * purge messages
     */
    void deleteQueue();

    /**
     * Clears the in memory hash set of created and available queues ( useful for tests that drop data and start over )
     */
    void clearQueueNameCache();
}
