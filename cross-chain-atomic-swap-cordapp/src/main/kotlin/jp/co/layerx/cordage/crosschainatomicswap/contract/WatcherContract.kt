package jp.co.layerx.cordage.crosschainatomicswap.contract

import jp.co.layerx.cordage.crosschainatomicswap.state.WatcherState
import net.corda.core.contracts.CommandData
import net.corda.core.contracts.Contract
import net.corda.core.contracts.requireSingleCommand
import net.corda.core.contracts.requireThat
import net.corda.core.transactions.LedgerTransaction

open class WatcherContract: Contract {
    companion object {
        const val contractID = "jp.co.layerx.cordage.crosschainatomicswap.contract.WatcherContract"
    }

    interface WatcherCommands : CommandData {
        class Watch : WatcherCommands
        class Issue : WatcherCommands
    }

    override fun verify(tx: LedgerTransaction) {
        val command = tx.commands.requireSingleCommand<WatcherCommands>()
        when (command.value) {
            is WatcherCommands.Issue -> requireThat {
                "No inputs should be consumed when issuing an Watcher." using (tx.inputs.isEmpty())
                "Only one output state should be created when issuing an Watcher." using (tx.outputs.size == 1)
                val watcher = tx.outputsOfType<WatcherState>().single()
                "A newly issued Watcher's fromBlockNumber must be more than zero." using (watcher.fromBlockNumber >= 0.toBigInteger())
                "The toBlockNumber must be greater than the fromBlockNumber." using (watcher.toBlockNumber > watcher.fromBlockNumber)
                "The targetContractAddress must start with 0x." using (watcher.targetContractAddress.startsWith("0x"))
            }
            is WatcherCommands.Watch -> requireThat {
                "Input state should be only one state." using (tx.inputs.size == 1)
                "Output state should be only one state." using (tx.outputs.size == 1)
                val input = tx.inputsOfType<WatcherState>().single()
                val output = tx.outputsOfType<WatcherState>().single()
                "The me property should not be change." using (input.me == output.me)
                "Output WatcherState must have a positive toBlockNumber." using (output.toBlockNumber > 0.toBigInteger())
                "Output's fromBlockNumber should be next number after input's toBlockNumber." using (input.toBlockNumber + 1.toBigInteger() == output.fromBlockNumber)
                "The toBlockNumber must be greater than the fromBlockNumber." using (output.toBlockNumber > output.fromBlockNumber)
                "The targetContractAddress property should not be change." using (input.targetContractAddress == output.targetContractAddress)
                "The eventName property should not be change." using (input.eventName == output.eventName)
                "The searchId property should not be change." using (input.proposalStateAndRef.state.data.swapId == output.proposalStateAndRef.state.data.swapId)
            }
        }
    }
}
