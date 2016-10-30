package repository

import java.lang.reflect.Field

import context.CoreContext

/**
  * * # Created by wacharint on 10/29/2016 AD.
  **/
class PointRedeemProfileRepository(implicit context: CoreContext) extends InjectAble
{
    var pointRedeemProfileId = 0
    var description = ""
    var pointInterval = 0
    var discountPerInterval = 0
    var minimumRedemption = 0

    override val callContext: CoreContext = context
    override val tableName: String = "point_redeem_profile"
    override var fields: Seq[Field] = classOf[PointRedeemProfileRepository].getDeclaredFields
}
