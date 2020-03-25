package jp.co.layerx.cordage.customnotaryflow.contracts

import jp.co.layerx.cordage.customnotaryflow.states.Agreement
import jp.co.layerx.cordage.customnotaryflow.states.AgreementStatus
import net.corda.core.contracts.CommandData
import net.corda.core.contracts.Contract
import net.corda.core.contracts.requireSingleCommand
import net.corda.core.contracts.requireThat
import net.corda.core.transactions.LedgerTransaction
import java.security.PublicKey

class AgreementContract: Contract {
    companion object {
        @JvmStatic
        val ID = this::class.java.enclosingClass.canonicalName!!
    }

    interface AgreementCommand: CommandData {
        class Make: AgreementCommand
        class Terminate: AgreementCommand
    }

    override fun verify(tx: LedgerTransaction) {
        val command = tx.commands.requireSingleCommand<AgreementCommand>()
        if (command.value is AgreementCommand.Make) verifyMaking(tx, command.signers)
        if (command.value is AgreementCommand.Terminate) verifyTerminate(tx, command.signers)
    }

    private fun verifyMaking(tx: LedgerTransaction, signers: List<PublicKey>) {
        requireThat {
            "No inputs" using (tx.inputs.isEmpty())
            "One output" using (tx.outputs.size == 1)
        }

        val agreement = tx.outputsOfType<Agreement>().single()
        requireThat {
            "origin is not equal to target" using (agreement.origin != agreement.target)
        }

        requireThat {
            "Agreement must be made" using (agreement.status == AgreementStatus.MADE)
            "Agreement must be signed by both party" using (signers.containsAll(agreement.participants.map { it.owningKey }))
        }
    }

    private fun verifyTerminate(tx: LedgerTransaction, signers: List<PublicKey>) {

    }
}
