/*
 * Copyright 2013 Ruben.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.velonuboso.made.core.rat;

/**
 *
 * @author Ruben
 */
public enum RatState {
    BORN,
    HUNGRY,
    EAT,
    NUDGE_OK,
    KILL,
    KILLED,
    NUDGED,
    NUDGE_FAILED,
    DEFENDED,
    KINDLY_DISPLACED,
    MOVE,
    MOVE_TO_EAT,
    LOOK_FOR_PARTNER,
    PARTNER_FOUND,
    PREGNANT,
    PARENT,
    DIE,
    FREE_TIME, 
    KINDLY_REQUESTED_FOOD
}
