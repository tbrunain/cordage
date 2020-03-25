package jp.co.layerx.cordage.customnotaryflow.states

import jp.co.layerx.cordage.customnotaryflow.contracts.AgreementContract
import net.corda.core.contracts.BelongsToContract
import net.corda.core.contracts.ContractState
import net.corda.core.identity.Party

@BelongsToContract(AgreementContract::class)
data class Agreement(
    val origin: Party,
    val target: Party,
    val status: AgreementStatus,
    val agreementBody: String
    ) : ContractState {
    override val participants = listOf(origin, target)

    fun terminate() = copy(status = AgreementStatus.TERMINATED)
}
