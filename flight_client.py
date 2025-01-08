import socket
import tkinter as tk
from tkinter import messagebox

def connect_to_server(origin, destination, output_box):
    host = "localhost"
    port = 12345

    try:
        with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
            s.connect((host, port))

            # Send data to the server
            s.sendall(origin.encode())
            s.sendall(b"\n")  # Newline to indicate end of input
            s.sendall(destination.encode())
            s.sendall(b"\n")

            # Receive and display response
            output_box.delete("1.0", tk.END)
            while True:
                data = s.recv(1024)
                if not data:
                    break
                output_box.insert(tk.END, data.decode())
    except ConnectionError as e:
        messagebox.showerror("Connection Error", f"Error connecting to server: {e}")

def search_flights():
    origin = origin_entry.get().strip()
    destination = destination_entry.get().strip()

    if not origin or not destination:
        messagebox.showwarning("Input Error", "Please enter both origin and destination.")
        return

    connect_to_server(origin, destination, output_box)

# Create the main application window
app = tk.Tk()
app.title("Flight Search App")

# Input fields for origin and destination
tk.Label(app, text="Origin:").grid(row=0, column=0, padx=10, pady=5, sticky="e")
origin_entry = tk.Entry(app, width=30)
origin_entry.grid(row=0, column=1, padx=10, pady=5)

tk.Label(app, text="Destination:").grid(row=1, column=0, padx=10, pady=5, sticky="e")
destination_entry = tk.Entry(app, width=30)
destination_entry.grid(row=1, column=1, padx=10, pady=5)

# Search button
search_button = tk.Button(app, text="Search Flights", command=search_flights)
search_button.grid(row=2, column=0, columnspan=2, pady=10)

# Output box for server response
output_box = tk.Text(app, height=10, width=50)
output_box.grid(row=3, column=0, columnspan=2, padx=10, pady=5)

# Run the application
app.mainloop()

