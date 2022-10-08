import {html, repeat, when, ref} from '@microsoft/fast-element';
import type {Home} from './home';
import {ColDef} from '@ag-grid-community/core';

export const positionsColumnDefs: ColDef[] = [
    {field: 'INSTRUMENT_ID', headerName: 'Instrument'},
    {field: 'QUANTITY', headerName: 'Quantity'},
    {field: 'NOTIONAL', headerName: 'Notional'},
    {field: 'VALUE', headerName: 'Value'},
    {field: 'PNL', headerName: 'Pnl'},
];

export const HomeTemplate = html<Home>`
<div class="split-layout">
    <div class="top-layout">
        <entity-management
          resourceName="ALL_TRADES"
          title = "Trades"
          entityLabel="Trades"
          createEvent = "EVENT_TRADE_INSERT"
          updateEvent = "EVENT_TRADE_MODIFY"
          deleteEvent = "EVENT_TRADE_CANCELLED"
          :columns=${x => x.columns}
          :permissions=${x => x.permissionsTrade}
        ></entity-management>
    </div>
    <div class="top-layout">
        <zero-card class="positions-card">
            <span class="card-title">Positions</span>
            <zero-ag-grid ${ref('positionsGrid')} rowHeight="45" only-template-col-defs>
                ${when(x => x.connection.isConnected, html`
                  <ag-genesis-datasource resourceName="ALL_POSITIONS"></ag-genesis-datasource>
                  ${repeat(() => positionsColumnDefs, html`
                    <ag-grid-column :definition="${x => x}" />
                  `)}
                `)}
            </zero-ag-grid>
        </zero-card>
    </div>
</div>
`;