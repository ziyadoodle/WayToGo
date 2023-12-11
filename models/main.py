import os
from fastapi import FastAPI
import uvicorn
from pydantic import BaseModel
import numpy as np
import pandas as pd
import tensorflow as tf
from sklearn.preprocessing import StandardScaler
from geopy.geocoders import GoogleV3

app = FastAPI()

# Initialize Model
model = tf.keras.models.load_model('./waytogo_model.h5')

scaler_location = StandardScaler()
geolocator = GoogleV3(api_key='AIzaSyD-ktXkaMsEdsxDQuMxJS0qit0O6GXvxQU')
dataset = pd.read_excel('WayToGo Dataset.xlsx')

# Pydantic model for request body
class Location(BaseModel):
    lat: float
    lng: float

# Preprocess user location
def preprocess_location(user_location):
    return scaler_location.transform(np.array(user_location).reshape(1, -1))


# Get kecamatan, kabupaten, & kota from formatted_address
def get_location_details_from_address(address):
    location = geolocator.geocode(address, language='id')
    if location and location.raw.get('address_components'):
        location_details = {
            'kecamatan': '',
            'kabupaten': '',
            'kota': ''
        }
        for component in location.raw['address_components']:
            if 'administrative_area_level_3' in component['types']:
                location_details['kecamatan'] = component['long_name']
            elif 'administrative_area_level_2' in component['types']:
                location_details['kabupaten'] = component['long_name']
            elif 'locality' in component['types']:
                location_details['kota'] = component['long_name']

        # Create city_name output
        formatted_output = f"{location_details['kecamatan']}, {location_details['kabupaten']}"
        if location_details['kota']:
            formatted_output = f"{location_details['kecamatan']}, {location_details['kabupaten']}, {location_details['kota']}"

        return formatted_output

    return None

# Add city_name to dataset
dataset['city_name'] = dataset['formatted_address'].apply(get_location_details_from_address)

# Calculate haversine distance
def haversine_vectorize(lon1, lat1, lon2, lat2):
    lon1, lat1, lon2, lat2 = map(np.radians, [lon1, lat1, lon2, lat2])

    newlon = lon2 - lon1
    newlat = lat2 - lat1

    haversine = np.sin(newlat / 2.0) ** 2 + np.cos(lat1) * np.cos(lat2) * np.sin(newlon / 2.0) ** 2

    dist = 2 * np.arcsin(np.sqrt(haversine))
    km = 6367 * dist  # 6367 is the radius of the Earth in kilometers

    return km

# Recommend places
def recommend_places(user_location, dataset, max_distance=10, top_k_distance=5, top_k_rating=5):
    # Hitung jarak dari lokasi pengguna ke setiap tempat
    dataset['distance'] = haversine_vectorize(user_location[1], user_location[0], dataset['lng'], dataset['lat'])

    # Filter tempat dengan jarak kurang dari atau sama dengan max_distance
    nearby_places = dataset[dataset['distance'] <= max_distance]

    # Jika tidak ada tempat di dalam radius, kembalikan pesan
    if nearby_places.empty:
        return "Tidak ada tempat di dalam radius jarak yang ditentukan."

    # Sort tempat berdasarkan jarak terdekat
    nearest_places = nearby_places.sort_values(by='distance').head(top_k_distance)

    # Replace NaN values in the 'rating' column with 0
    dataset['rating'] = dataset['rating'].fillna(0)

    # Sort seluruh tempat berdasarkan rating tertinggi
    top_rated_places = dataset.sort_values(by='rating', ascending=False).head(top_k_rating)

    return nearest_places[['place_id','name','city_name', 'photo_reference', 'distance']], top_rated_places[['place_id','name','city_name', 'photo_reference']]

@app.get("/")
def index():
    return {"message": "WayToGo Model"}

@app.post("/popular_in_your_area")
def get_popular_in_your_area(location: Location):
    user_location = (location.lat, location.lng)
    max_distance = 10  
    top_k_distance = 5  
    top_k_rating = 5

    nearest_places, top_rated_places = recommend_places(user_location, dataset, max_distance, top_k_distance, top_k_rating)
    result = nearest_places.to_dict(orient='records')
    return result

@app.post("/popular_destination")
def get_popular_destination(location: Location):
    user_location = (location.lat, location.lng)
    max_distance = 10  
    top_k_distance = 5  
    top_k_rating = 5

    nearest_places, top_rated_places = recommend_places(user_location, dataset, max_distance, top_k_distance, top_k_rating)
    top_rated_places = top_rated_places.replace([np.inf, -np.inf], np.nan)
    top_rated_places = top_rated_places.fillna(0)  # Replace NaN with a valid value
    result = top_rated_places.to_dict(orient='records')
    return result

def start_fastapi_server():
    port = os.environ.get("PORT", 8080)
    print(f"Listening to http://0.0.0.0:{port}")
    uvicorn.run(app, host='0.0.0.0', port=port)

if __name__ == "__main__":
    start_fastapi_server()