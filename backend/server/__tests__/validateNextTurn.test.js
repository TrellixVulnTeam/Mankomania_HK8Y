const { createServer } = require("http");
const { Server } = require("socket.io");
const Client = require("socket.io-client");
import * as backend from '../server.js'
import MockedSocket from 'socket.io-mock';


describe('Test validateNextTurn function', function(){
    let io, serverSocket, clientSocket
    let roomID = "TEST_ROOM"
    let rooms = {};
    let mockSocket = new MockedSocket();


    beforeAll((done) => {
        const httpServer = createServer();
        io = new Server(httpServer);
        httpServer.listen(() => {
            const port = httpServer.address().port;
            clientSocket = new Client(`http://localhost:${port}`);
            io.on("connection", (socket) => {
                serverSocket = socket;
                clientSocket.join(roomID)
            });
            clientSocket.on("connect", done);
            mockSocket.on("connect", done);

        });

    });


    afterAll(() => {
        io.close();
        clientSocket.close();

    });

    test('test function validateNextTurn with invalid _rooms parameter', ()=>{
        clientSocket.on('ERROR', (arg) =>{
            expect(arg).toMatch("Error in validateNextTurn")
        });
        backend.validateNextTurn(null, roomID, serverSocket)
    });

    test('test function validateNextTurn with invalid room parameter', ()=>{
        setup()
        clientSocket.on('ERROR', (arg) =>{
            expect(arg).toMatch("Error in validateNextTurn")
        });
        backend.validateNextTurn(rooms, null, serverSocket)
    });


    test('test function validateNextTurn with valid parameters', ()=>{
        setup()
        clientSocket.on('NEXT_TURN', (arg) =>{
            expect(arg).toBe(mockSocket.id)
        });
        backend.validateNextTurn(rooms[roomID], roomID, serverSocket)
    });


    const setup = () =>{
        const room = {
            id: roomID, // generate a unique id for the new room, that way we don't need to deal with duplicates.
            status: false,
            sockets: [],
            ready: 0,
            players: {},
            stockCounterFunction : function (room, socket, stock){
                rooms[room].players[socket.id].stocks = stock
                this.counterForStocks++
            },
            counterForStocks : 0,
            counterForDice: 0,
            counterForHorseRace: 0
        };
        rooms[room.id] = room;

        const stocks = {
            HardSteel_PLC: 2,
            ShortCircuit_PLC: 2,
            DryOil_PLC : 2
        }

        rooms[room.id].players[clientSocket.id] = {
            socket: clientSocket.id,
            playerIndex: 1,
            money: 1000000,
            position: 2,
            stocks: stocks,
            yourTurn: false,
            dice_1: 0,
            dice_2: 0,
            dice_Count: 0,
            calculateDiceCount: function (diceCount) {
                this.dice_1 = diceCount[0]
                this.dice_2 = diceCount[1]
                this.dice_Count = this.dice_1 + this.dice_2
            }
        }

        rooms[room.id].players[mockSocket.id] = {
            socket: mockSocket.id,
            playerIndex: 1,
            money: 1000000,
            position: 2,
            stocks: stocks,
            yourTurn: false,
            dice_1: 0,
            dice_2: 0,
            dice_Count: 0,
            calculateDiceCount: function (diceCount) {
                this.dice_1 = diceCount[0]
                this.dice_2 = diceCount[1]
                this.dice_Count = this.dice_1 + this.dice_2
            }
        }


    }


});


