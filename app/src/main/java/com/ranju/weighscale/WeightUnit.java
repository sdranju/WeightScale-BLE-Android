package com.ranju.weighscale;

import androidx.annotation.NonNull;


public enum WeightUnit {
    Kilograms {
        @NonNull
        @Override
        public String toString() {
            return "Kg";
        }
    },
    Pounds {
        @NonNull
        @Override
        public String toString() {
            return "lbs";
        }
    }
}
