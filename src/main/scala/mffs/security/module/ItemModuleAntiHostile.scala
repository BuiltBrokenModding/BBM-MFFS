package mffs.security.module

import mffs.ModularForceFieldSystem
import net.minecraft.entity.{EntityLivingBase, INpc}
import net.minecraft.entity.monster.IMob
import resonant.api.mffs.security.IInterdictionMatrix

class ItemModuleAntiHostile extends ItemModuleDefense
{
  override def onDefend(interdictionMatrix: IInterdictionMatrix, entityLiving: EntityLivingBase): Boolean =
  {
    if (entityLiving.isInstanceOf[IMob] && !(entityLiving.isInstanceOf[INpc]))
    {
      entityLiving.attackEntityFrom(ModularForceFieldSystem.damageFieldShock, 20)
    }
    return false
  }
}