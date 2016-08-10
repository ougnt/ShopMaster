package repository

import java.lang.reflect.Field

import context.CoreContext

/**
  * * # Created by wacharint on 8/10/2016 AD.
  **/
class PointRedeemOptionRepository(implicit context: CoreContext) extends InjectAble
{
    var redeem_option_id = 0
    var point = 0
    var discount = 0
    recStatus = 1

    override val callContext: CoreContext = context
    override val tableName: String = "point_redeem_options"
    override var fields: Seq[Field] = classOf[PointRedeemOptionRepository].getDeclaredFields
}
