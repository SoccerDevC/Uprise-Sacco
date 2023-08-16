<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    /**
     * Run the migrations.
     */
    public function up(): void
    {
        Schema::create('registered_loans', function (Blueprint $table) {
            $table->id();
            $table->integer('loan_application_number');
            $table->integer('payment_period');
            $table->date('installment_date');
            $table->string('member_id');

            $table->timestamps();
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('registered_loans');
    }
};
