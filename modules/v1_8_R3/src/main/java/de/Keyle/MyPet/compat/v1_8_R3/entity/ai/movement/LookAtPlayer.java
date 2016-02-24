/*
 * This file is part of mypet-v1_8_R3
 *
 * Copyright (C) 2011-2016 Keyle
 * mypet-v1_8_R3 is licensed under the GNU Lesser General Public License.
 *
 * mypet-v1_8_R3 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mypet-v1_8_R3 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package de.Keyle.MyPet.compat.v1_8_R3.entity.ai.movement;

import de.Keyle.MyPet.api.entity.ai.AIGoal;
import de.Keyle.MyPet.compat.v1_8_R3.entity.EntityMyPet;
import net.minecraft.server.v1_8_R3.Entity;

public class LookAtPlayer extends AIGoal {
    private EntityMyPet petEntity;
    protected Entity targetPlayer;
    private float range;
    private int ticksUntilStopLooking;
    private float lookAtPlayerChance;

    public LookAtPlayer(EntityMyPet petEntity, float range) {
        this.petEntity = petEntity;
        this.range = range;
        this.lookAtPlayerChance = 0.02F;
    }

    public LookAtPlayer(EntityMyPet petEntity, float range, float lookAtPlayerChance) {
        this.petEntity = petEntity;
        this.range = range;
        this.lookAtPlayerChance = lookAtPlayerChance;
    }

    @Override
    public boolean shouldStart() {
        if (this.petEntity.getRandom().nextFloat() >= this.lookAtPlayerChance) {
            return false;
        }
        if (this.petEntity.getGoalTarget() != null && this.petEntity.getGoalTarget().isAlive()) {
            return false;
        }
        if (this.petEntity.passenger != null) {
            return false;
        }
        this.targetPlayer = this.petEntity.world.findNearbyPlayer(this.petEntity, this.range);
        return this.targetPlayer != null;
    }

    @Override
    public boolean shouldFinish() {
        if (!this.targetPlayer.isAlive()) {
            return true;
        }
        if (this.petEntity.h(this.targetPlayer) > this.range) {
            return true;
        }
        if (this.petEntity.passenger != null) {
            return true;
        }
        return this.ticksUntilStopLooking <= 0;
    }

    @Override
    public void start() {
        this.ticksUntilStopLooking = (40 + this.petEntity.getRandom().nextInt(40));
    }

    @Override
    public void finish() {
        this.targetPlayer = null;
    }

    @Override
    public void tick() {
        this.petEntity.getControllerLook().a(this.targetPlayer.locX, this.targetPlayer.locY + this.targetPlayer.getHeadHeight(), this.targetPlayer.locZ, 10.0F, this.petEntity.bQ());
        this.ticksUntilStopLooking -= 1;
    }
}